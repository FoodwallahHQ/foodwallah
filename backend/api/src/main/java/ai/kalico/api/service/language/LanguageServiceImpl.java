package ai.kalico.api.service.language;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.dto.ClusterItem;
import ai.kalico.api.dto.GptResponse;
import ai.kalico.api.dto.Pair;
import ai.kalico.api.dto.RecipeGptResponse;
import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.openai.OpenAiService;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import ai.kalico.api.service.openai.completion.CompletionRequest;
import ai.kalico.api.service.utils.Slugify;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ContentItem;
import com.kalico.model.ContentItemChildren;
import com.kalico.model.KalicoContentType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 1/30/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
  private final MediaContentRepo mediaContentRepo;
  private final ProjectRepo projectRepo;
  private final OpenAiProps openAiProps;
  private OpenAiService openAiService;
  private final ObjectMapper objectMapper;
  private final RecipeRepo recipeRepo;

  @PostConstruct
  public void onStart() {
    openAiService = new OpenAiService(openAiProps.getApiKey(),
        openAiProps.getRequestTimeoutSeconds());
  }

  @Override
  public List<ContentItem> generateContent(Long projectId, String mediaId, boolean recipeContent) {
    if (recipeContent) {
      generateRecipeContent(mediaId);
      return new ArrayList<>();
    }
    MediaContentEntity contentEntity = mediaContentRepo.findByProjectId(projectId);
    long then = Instant.now().toEpochMilli();
    if (contentEntity != null) {
      if (!ObjectUtils.isEmpty(contentEntity.getRawTranscript()) &&
          contentEntity.getRawTranscript().length() > 0) {
        log.info("LanguageServiceImpl.generateContent Starting content generation for projectId={}",
            projectId);
        KalicoContentType contentType = KalicoContentType.OTHER;
        Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(
            contentEntity.getProjectId());
        String description = contentEntity.getScrapedDescription();
        boolean onlyGetRawTranscript = false;
        if (projectEntityOpt.isPresent()) {
          contentType = KalicoContentType.fromValue(projectEntityOpt.get().getContentType());
          onlyGetRawTranscript = projectEntityOpt.get().getGetRawTranscript();
        }

        if (description != null
            && description.length() > openAiProps.getDescriptionCharacterLimit()) {
          // Extract details from the description
          // TODO: consolidate the description and the transcript
        }
        List<ContentItem> content = new ArrayList<>();
        if (onlyGetRawTranscript) {
          content = generateRawTranscriptContent(contentEntity.getRawTranscript(),
              projectEntityOpt.get().getProjectName());
        } else {
          // Break input transcript into clusters
          List<String> chunkedTranscript = chunkTranscript(
              cleanup(contentEntity.getRawTranscript()),
              openAiProps.getChunkSize());
          log.info("LanguageServiceImpl.generateContent Clustering transcript for projectId={}",
              projectId);
          List<GptResponse> clusters = clusterTranscript(chunkedTranscript);
          List<ClusterItem> clustersWithHeadings;
          // Do no proceed if there are no clusters
          if (!ObjectUtils.isEmpty(clusters) &&
              !ObjectUtils.isEmpty(clusters.get(0).getCompletionChoices())) {
            clustersWithHeadings = getClustersWithHeadings(clusters);

            // Each cluster is broken down into paragraphs and the grammar and writing style is
            // improved. This acts as a section in the final output with its own subheading.
            // Subheadings are needed for longer texts, depending on the niche.
            // TODO: We can correlate the paragraphs back to the timestamped transcript to determine
            //      the best image or GIF for it automatically
            log.info("LanguageServiceImpl.generateContent Generating paragraphs for projectId={}",
                projectId);
            List<ClusterItem> paragraphsByCluster = clustersIntoParagraphs(clustersWithHeadings);

            // Generate a title using the first chunk of the transcript. This eats into the token usage
            // because we have to send the entire chunk for context.
            // TODO: Design a better prompt to generate the title and do the cluster at the same time
            log.info("LanguageServiceImpl.generateContent Generating title for projectId={}",
                projectId);
            GptResponse title = gptCompletion(0, openAiProps.getPromptTitle(),
                chunkedTranscript.get(0), null);

            GptResponse recipe = null;
            if (contentType != null && contentType.equals(KalicoContentType.FOOD_RECIPE)) {
              // Extract recipe information
              // TODO: Not sure how to reconcile food recipes where the detail is spread across multiple chunks
              log.info("LanguageServiceImpl.generateContent Generating recipe for projectId={}",
                  projectId);
              recipe = gptCompletion(0, openAiProps.getPromptRecipe(),
                  chunkedTranscript.get(0), null);
            }
            log.info(
                "LanguageServiceImpl.generateContent Generating structured content projectId={}",
                projectId);
            content = generateContent(title, paragraphsByCluster, recipe);
          }
        }
        saveContent(contentEntity.getProjectId(), content);

        // Log total time
        long now = Instant.now().toEpochMilli();
        double minutes = Math.round(((now - then)/(1000*60.0)) * 100)/100.0;
        log.info(
            "LanguageServiceImpl.generateContent Finished content generation for projectId={}. "
                + "Total time: {} minutes",
            projectId,
            minutes);

        return content;
      } else {
        setProjectFailure(contentEntity.getProjectId(), "Submitted audio/video does not contain any spoken words");
      }
    }

    log.info("LanguageServiceImpl.generateContent Failed to start content generation for projectId={}", projectId);
    return new ArrayList<>();
  }

  private void generateRecipeContent(String contentId) {
    Optional<RecipeEntity> recipeEntityOpt = recipeRepo.findByContentId(contentId);
    if (recipeEntityOpt.isPresent()) {
      RecipeEntity recipeEntity = recipeEntityOpt.get();
      log.info("LanguageServiceImpl.generateRecipeContent Starting content generation for contentId={}",
          contentId);
      List<String> chunkedTranscript = chunkTranscript(
          cleanup(recipeEntity.getTranscript()),
          openAiProps.getRecipe().getChunkSize());
      // Use just the first chunk of the recipe transcript. Chunks are about 2048 words long,
      // which is a lot for any food recipe. It's also hard to consolidate technical recipe
      // information contained across several chunks. Therefore, using just the first chunk
      // will ensure that the final content is self-consistent. GPT-4 has an 8K token window,
      // which we can use it becomes necessary. For now, the 4K token window in GPT-3 is
      // sufficient.
      var chunk = chunkedTranscript.get(0);
      if (openAiProps.getRecipe().getConsolidatedPrompt() != null) {
        save(extractRecipeGptResponse(
            recipeGptCompletion(
                openAiProps.getRecipe().getConsolidatedPrompt(), chunk)), 
            contentId);
      } else {
        asyncRecipeRequests(chunk, contentId);
      }
    }
  }

  @Override
  public RecipeGptResponse extractRecipeGptResponse(GptResponse recipeGptCompletion) {
    String text = recipeGptCompletion.getCompletionChoices().get(0).getText();
    log.info("LanguageServiceImpl.extractRecipeGptResponse Extracting content from completion text: {}",
        text);
    if (text != null) {
      var lines = new ArrayList<>(List.of(text.split("\n")));
      var title = extractTitle(lines);
      var summary = extractSummary(lines);
      var ingredients = extractIngredients(lines);
      var instructions = extractInstructions(lines);
      log.info("LanguageServiceImpl.extractRecipeGptResponse Extracted title: {}",
          title);
      log.info("LanguageServiceImpl.extractRecipeGptResponse Extracted summary: {}",
          summary);
      log.info("LanguageServiceImpl.extractRecipeGptResponse Extracted ingredients: {}",
          String.join("\n", ingredients));
      log.info("LanguageServiceImpl.extractRecipeGptResponse Extracted instructions: {}",
          String.join("\n", instructions));
      return new RecipeGptResponse(title, summary, ingredients, instructions);
    }
    return new RecipeGptResponse("", "", new ArrayList<>(), new ArrayList<>());
  }

  private List<String> extractIngredients(List<String> lines) {
    List<String> ingredients = new ArrayList<>();
    boolean ingredientHeaderFound = false;
    for (int i = 0; i < lines.size(); i++) {
      var lower = lines.get(i).toLowerCase();
      if (lower.startsWith("ingredient")) {
        ingredientHeaderFound = true;
        var text = cleanup(lines.get(i));
        if (!ObjectUtils.isEmpty(text)) {
          // The ingredients are on the same line so break them apart
          return new ArrayList<>(List.of(text.split(",")))
              .stream()
              .map(this::cleanup)
              .filter(it -> !ObjectUtils.isEmpty(it))
              .collect(Collectors.toList());
        }
      } else if (ingredientHeaderFound && lower.startsWith("-")) {
        ingredients.add(cleanup(lines.get(i).replace("-", "")));
      }
    }
    return ingredients;
  }

  private List<String> extractInstructions(List<String> lines) {
    List<String> instructions = new ArrayList<>();
    boolean instructionHeaderFound = false;
    for (int i = 0; i < lines.size(); i++) {
      var lower = lines.get(i).toLowerCase();
      if (lower.startsWith("instruction") ||
          lower.startsWith("step") ||
          lower.startsWith("recipe step")) {
        instructionHeaderFound = true;
      } else if (instructionHeaderFound && startsWithNumber(lower)) {
        instructions.add(cleanup(lines.get(i)));
      }
    }
    return instructions;
  }


  private String extractSummary(List<String> lines) {
    boolean summaryHeaderFound = false;
    var body = "";
    for (int i = 0; i< lines.size(); i++) {
      var lower = lines.get(i).toLowerCase();
      if (lower.startsWith("summary")) {
        summaryHeaderFound = true;

        // The summary may appear on the same line as the header itself
        body = cleanup(lines.get(i));
        if (!ObjectUtils.isEmpty(body)) {
          break;
        }
      } else if (summaryHeaderFound && !ObjectUtils.isEmpty(cleanup(lower))) {
        // Consider the next non-empty line that comes after the summary header to be
        // the summary itself.
        body = cleanup(lines.get(i));
        break;
      }
    }
    return body;
  }

  private String extractTitle(List<String> lines) {
    for (int i = 0; i< lines.size(); i++) {
      var lower = lines.get(i).toLowerCase();
      if (lower.contains("title")) {
        return cleanup(lines.get(i));
      }
    }
    return "";
  }

  private void asyncRecipeRequests(String context, String contentId) {
    ConcurrentHashMap<String, GptResponse> asyncResponse = new ConcurrentHashMap<>();
    List<CompletableFuture<Void>> tasks = new ArrayList<>(List.of(
        CompletableFuture.runAsync(() -> generateRecipeTitle(context, asyncResponse), RootConfiguration.executor),
        CompletableFuture.runAsync(() -> generateRecipeSummary(context, asyncResponse), RootConfiguration.executor),
        CompletableFuture.runAsync(() -> generateRecipeIngredients(context, asyncResponse), RootConfiguration.executor),
        CompletableFuture.runAsync(() -> generateRecipeInstructions(context, asyncResponse), RootConfiguration.executor)
    ));
    awaitVoid(tasks);
    saveRecipeContent(asyncResponse, contentId);
  }

  private void saveRecipeContent(ConcurrentHashMap<String, GptResponse> asyncResponse, String contentId) {
    GptResponse titleResponse = asyncResponse.get(RecipePartName.TITLE);
    GptResponse summaryResponse = asyncResponse.get(RecipePartName.SUMMARY);
    GptResponse ingredientResponse = asyncResponse.get(RecipePartName.INGREDIENTS);
    GptResponse instructionResponse = asyncResponse.get(RecipePartName.INSTRUCTIONS);

    var title = cleanup(extractTitle(titleResponse.getCompletionChoices().get(0).getText()));
    var summary = cleanup(summaryResponse.getCompletionChoices().get(0).getText());
    var ingredientsText = ingredientResponse.getCompletionChoices().get(0).getText();
    var instructionsText = instructionResponse.getCompletionChoices().get(0).getText();
    List<String> ingredients = extractIngredients(ingredientsText);
    List<String> instructions = extractInstructions(instructionsText);
    save(new RecipeGptResponse(
        title,
        summary,
        ingredients,
        instructions), contentId);
  }

  private void save(RecipeGptResponse recipeGptResponse, String contentId) {
    Optional<RecipeEntity> recipeEntityOpt = recipeRepo.findByContentId(contentId);
    if (recipeEntityOpt.isPresent()) {
      RecipeEntity recipeEntity = recipeEntityOpt.get();

      recipeEntity.setSummary(recipeGptResponse.getSummary());
      boolean processed = true;
      var reasonFailed = ";";
      if (ObjectUtils.isEmpty(recipeGptResponse.getTitle())) {
        processed = false;
        reasonFailed = "Unable to generate a title";
      } else {
        recipeEntity.setTitle(recipeGptResponse.getTitle());
        recipeEntity.setSlug(Slugify.builder().build().slugify(recipeGptResponse.getTitle()));
      }
      try {
        var ingredients = recipeGptResponse.getIngredients();
        recipeEntity.setNumIngredients(ingredients.size());
        if (ingredients.size() == 0) {
          processed = false;
          reasonFailed = "There are no ingredients in the video submitted";
        }
        recipeEntity.setIngredients(objectMapper.writeValueAsString(ingredients));
      } catch (JsonProcessingException e) {
        log.error("LanguageServiceImpl.save {}", e.getLocalizedMessage());
        processed = false;
        reasonFailed = "Failed to extract ingredients";
      }
      try {
        var instructions = recipeGptResponse.getInstructions();
        recipeEntity.setNumSteps(instructions.size());
        if (instructions.size() == 0) {
          processed = false;
          reasonFailed = "There are no recipe instructions in the video submitted";
        }
        recipeEntity.setInstructions(objectMapper.writeValueAsString(instructions));
      } catch (JsonProcessingException e) {
        log.error("LanguageServiceImpl.saveRecipeContent {}", e.getLocalizedMessage());
        processed = false;
        reasonFailed = "Failed to extract recipe instructions";
      }
      if (!processed) {
        recipeEntity.setProcessed(false);
        recipeEntity.setFailed(true);
        recipeEntity.setReasonFailed(reasonFailed);
        log.error("LanguageServiceImpl.save {} for contentId = {}", reasonFailed, contentId);
      } else {
        recipeEntity.setProcessed(true);
      }
      recipeEntity.setUpdatedAt(LocalDateTime.now());
      recipeRepo.save(recipeEntity);
      log.info("LanguageServiceImpl.save Finished processing for contentId = {}", contentId);
    }
  }

  private List<String> extractIngredients(String text) {
    List<String> lines = List.of(text.split("\n"));
    List<String> result = new ArrayList<>();
    for (String line : lines) {
      var cleanLine = cleanup(line).replace("-", "");
      if (!ObjectUtils.isEmpty(cleanLine)) {
        result.add(cleanLine);
      }
    }
    return result;
  }

  private List<String> extractInstructions(String text) {
    List<String> lines = List.of(text.split("\n"));
    List<String> result = new ArrayList<>();
    for (String line : lines) {
      var cleanLine = cleanup(line);
      if (!ObjectUtils.isEmpty(cleanLine)) {
        result.add(cleanLine);
      }
    }
    return result;
  }

  private void generateRecipeTitle(String context, ConcurrentHashMap<String, GptResponse> asyncResponse) {
    asyncResponse.put(RecipePartName.TITLE, recipeGptCompletion(openAiProps.getRecipe().getTitle(), context));
  }

  private void generateRecipeSummary(String context, ConcurrentHashMap<String, GptResponse> asyncResponse) {
    asyncResponse.put(RecipePartName.SUMMARY, recipeGptCompletion(openAiProps.getRecipe().getSummary(), context));
  }

  private void generateRecipeIngredients(String context, ConcurrentHashMap<String, GptResponse> asyncResponse) {
    asyncResponse.put(RecipePartName.INGREDIENTS, recipeGptCompletion(openAiProps.getRecipe().getIngredients(), context));
  }

  private void generateRecipeInstructions(String context, ConcurrentHashMap<String, GptResponse> asyncResponse) {
    asyncResponse.put(RecipePartName.INSTRUCTIONS, recipeGptCompletion(openAiProps.getRecipe().getInstructions(), context));
  }

  private List<ClusterItem> getClustersWithHeadings(List<GptResponse> clusters) {
    // Extract the cluster header and raw text from each cluster
    List<ClusterItem> items = new ArrayList<>();
    int i = 0;
    for (GptResponse cluster : clusters) {
      for (CompletionChoice completionChoice : cluster.getCompletionChoices()) {
        // Treat each completion choice as a cluster
        String fullText = completionChoice.getText();
        if (fullText != null) {
          List<String> groups = Arrays
              .stream(fullText.split("\n"))
              .filter(it -> !ObjectUtils.isEmpty(it))
              .collect(Collectors.toList());
          // Go through the groups and extract the heading and raw text

          String title = null;
          String rawText = null;
          for (String group : groups) {
            group = group.trim();
            if (!ObjectUtils.isEmpty(group)) {
              if (isTitle(group)) {
                title = group;
              } else {
                if (title != null) {
                  // The title must not be null when assigning the raw text. This is necessary to deal with
                  // cases where GptCompletion returns a stray cluster without a corresponding title. Such
                  // cluster is found at the very beginning of the completion response. It does not belong
                  // in the rest of the text.
                  // NB: This fix may be an over-correction and may result in some weird bugs down the line
                  rawText = group;
                }
              }
              if (title != null && rawText != null) {
                items.add(new ClusterItem(title, rawText, new ArrayList<>(), i));
                title = null;
                rawText = null;
                i++;
              }
            }
          }
        }
      }
    }
    return items;
  }

  private boolean isTitle(String text) {
    // Perform a naive title check
    return text.charAt(text.length() - 1) != '.';
  }

  private List<ContentItem> generateRawTranscriptContent(String rawTranscript, String projectName) {
    List<ContentItem> content = new ArrayList<>();
    content.add(new ContentItem()
        .type("title")
        .children(List.of(new ContentItemChildren()
            .text(projectName))));
    content.add(new ContentItem()
        .type("paragraph")
        .children(List.of(new ContentItemChildren()
            .text(rawTranscript))));
    return content;
  }
  private List<ContentItem> generateContent(GptResponse title, List<ClusterItem> paragraphsByCluster,
      GptResponse recipe) {
    List<ContentItem> content = new ArrayList<>();
    if (title != null && !ObjectUtils.isEmpty(title.getCompletionChoices())) {
      ContentItem item = new ContentItem()
          .type("title")
          .children(List.of(new ContentItemChildren()
              .text(cleanup(extractTitle(title.getCompletionChoices().get(0).getText())))));
      content.add(item);
    }
    // Sort the clusters
    if (!ObjectUtils.isEmpty(paragraphsByCluster)) {
      paragraphsByCluster.sort(Comparator.comparing(ClusterItem::getSortOrder));
      for (ClusterItem clusterItem : paragraphsByCluster) {
        // TODO: This is where we insert the section headers
        if (!ObjectUtils.isEmpty(clusterItem.getTitle())) {
          content.add(new ContentItem()
              .type("heading")
              .children(List.of(new ContentItemChildren()
                  .text(cleanup(clusterItem.getTitle())))));
        }
        // If this cluster doesn't have paragraphs, then use the raw text
        if (ObjectUtils.isEmpty(clusterItem.getParagraphs())) {
          content.add(new ContentItem()
              .type("paragraph")
              .children(List.of(new ContentItemChildren()
                  .text(cleanup(clusterItem.getRawText())))));
        } else {
          for (String paragraph : clusterItem.getParagraphs()) {
            content.add(new ContentItem()
                .type("paragraph")
                .children(List.of(new ContentItemChildren()
                    .text(paragraph))));
          }
        }
      }
    }
    content.addAll(generateRecipe(recipe));

    return content;
  }

  private String extractTitle(String text) {
    // The title sometimes comes back with a few sentences from the input. It's not clear why.
    String[] tokens = text.split("\n");
    List<String> validStrings = new ArrayList<>(List.of(text.split("\n")))
        .stream()
        .filter(it -> !ObjectUtils.isEmpty(cleanup(it)))
        .collect(Collectors.toList());
    // The last line is the title
    return validStrings.get(validStrings.size() - 1);
  }

  private List<ContentItem> generateRecipe(GptResponse recipe) {
    List<ContentItem> content = new ArrayList<>();
    if (recipe != null && !ObjectUtils.isEmpty(recipe.getCompletionChoices())) {
      List<String> recipeDetails = Stream.of(recipe.getCompletionChoices().get(0)
              .getText().split("\n"))
          .filter(text -> !ObjectUtils.isEmpty(cleanup(text))).collect(Collectors.toList());
      // Ingredients start with a hyphen and steps start with a number
      List<String> ingredients = recipeDetails
          .stream()
          .filter(it -> it.startsWith("-")).
          map(it -> it.replace("-", "")).collect(
              Collectors.toList());
      List<String> steps = recipeDetails
          .stream()
          .filter(this::startsWithNumber)
          .map(it -> it + "\n")
          .collect(Collectors.toList());

      content.add( new ContentItem()
          .type("heading")
          .children(List.of(new ContentItemChildren()
              .text("Ingredients"))));

      content.addAll(ingredients
          .stream()
          .map(it -> new ContentItem()
              .type("check-list-item")
              .checked(false)
              .children(List.of(new ContentItemChildren()
                  .text(it)))).
          collect(Collectors.toList()));

      content.add( new ContentItem()
          .type("heading")
          .children(List.of(new ContentItemChildren()
              .text("Recipe Steps"))));

      content.add( new ContentItem()
          .type("paragraph")
          .children(steps
              .stream()
              .map(it -> new ContentItemChildren()
                  .text(it))
              .collect(Collectors.toList())));
    }
    return content;
  }

  private boolean startsWithNumber(String input) {
    String pattern = "(^\\d)";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(input);
    return m.find();
  }


  private List<ClusterItem> clustersIntoParagraphs(List<ClusterItem> clusters) {
    List<ClusterItem> response = new ArrayList<>();
    List<CompletableFuture<GptResponse>> tasks = new ArrayList<>();
    for (final ClusterItem cluster : clusters) {
      final String context = cluster.getRawText();
      final int sortingOrder = cluster.getSortOrder();
      // Break any cluster with more characters than the threshold into paragraphs
      if (context.length() > openAiProps.getParagraphThresholdCharCount()) {
        tasks.add(CompletableFuture
            .supplyAsync(
                () -> gptCompletion(sortingOrder, openAiProps.getPromptParagraph(), context, cluster),
                RootConfiguration.executor));
      } else {
        response.add(cluster);
      }
    }

    List<GptResponse> gptResponse =  await(tasks);
    // Extract the cluster items and add the responses to paragraphs
    for (GptResponse hydratedResponse : gptResponse) {
      List<String> paragraphs = extractParagraphs(hydratedResponse);
      ClusterItem clusterItem = hydratedResponse.getClusterItem();
      if (clusterItem != null) {
        clusterItem.setParagraphs(paragraphs);
        response.add(clusterItem);
      }
    }
    return response;
  }

  private List<String> extractParagraphs(GptResponse hydratedResponse) {
    List<String> paragraphs = new ArrayList<>();
    if (hydratedResponse != null && !ObjectUtils.isEmpty(hydratedResponse.getCompletionChoices())) {
      for (CompletionChoice completionChoice : hydratedResponse.getCompletionChoices()) {
        List<String> paragraphsByCompletionChoice = Arrays
            .stream(completionChoice.getText().split("\n\n"))
            .filter(it -> !ObjectUtils.isEmpty(it))
            .map(this::cleanup)
            .collect(Collectors.toList());
        paragraphs.addAll(paragraphsByCompletionChoice);
      }
    }
    return paragraphs;
  }


  private List<GptResponse> clusterTranscript(List<String> chunkedTranscript) {
    List<CompletableFuture<GptResponse>> tasks = new ArrayList<>();
    for (int i = 0; i < chunkedTranscript.size(); i++) {
      final int soringOrder = i;
      final String chunk = chunkedTranscript.get(i);
      tasks.add(CompletableFuture
          .supplyAsync(() -> gptCompletion(soringOrder, openAiProps.getPromptCluster(), chunk, null),
              RootConfiguration.executor));
    }
    return await(tasks);
  }

  private  GptResponse gptCompletion(int sortOrder, String prompt, String context, ClusterItem clusterItem) {
    String query = String.format("%s\n%s", prompt, cleanup(context));
    CompletionRequest completionRequest = CompletionRequest.builder()
        .model(openAiProps.getModel())
        .prompt(query)
        .echo(false)
        .temperature(openAiProps.getTemp())
        .n(1)
        .maxTokens(openAiProps.getMaxTokens())
        .user(openAiProps.getUser())
        .logitBias(new HashMap<>())
        .build();
    int retried = 0;
    while (retried < openAiProps.getNumRetries()) {
      try {
        return new GptResponse(sortOrder, openAiService.createCompletion(completionRequest).getChoices(), clusterItem);
      } catch (Exception e) {
        // Catch network timeout errors and retry
        try {
          // Sleep for 5 seconds
          Thread.sleep(5000L);
        } catch (InterruptedException ex) {
          // ignore
        }
        retried++;
        log.error("LanguageServiceImpl.gptCompletion {}", e.getLocalizedMessage());
      }
    }
    return new GptResponse(0, new ArrayList<>(), null);
  }

  private  GptResponse recipeGptCompletion(String prompt, String context) {
    String query = String.format("%s\n%s", prompt, cleanup(context));
    CompletionRequest completionRequest = CompletionRequest.builder()
        .model(openAiProps.getModel())
        .prompt(query)
        .echo(false)
        .temperature(openAiProps.getRecipe().getTemp())
        .n(1)
        .maxTokens(openAiProps.getRecipe().getMaxTokens())
        .user(openAiProps.getUser())
        .logitBias(new HashMap<>())
        .build();
    int retried = 0;
    while (retried < openAiProps.getNumRetries()) {
      try {
        log.info("LanguageServiceImpl.recipeGptCompletion Making GPT completion request for prompt '{}",
            prompt);
        return new GptResponse(0, openAiService.createCompletion(completionRequest).getChoices(), null);
      } catch (Exception e) {
        // Catch network timeout errors and retry
        try {
          // Sleep for 5 seconds
          Thread.sleep(5000L);
        } catch (InterruptedException ex) {
          // ignore
        }
        retried++;
        log.error("LanguageServiceImpl.recipeGptCompletion {}", e.getLocalizedMessage());
      }
    }
    return new GptResponse(0, new ArrayList<>(), null);
  }

  private List<GptResponse> await(List<CompletableFuture<GptResponse>> tasks) {
    return tasks
        .stream()
        .map(CompletableFuture::join)
        .sorted(Comparator.comparing(GptResponse::getSortOrder))
        .collect(Collectors.toList());
  }

  private List<Void> awaitVoid(List<CompletableFuture<Void>> tasks) {
    return tasks
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  @Override
  public String cleanup(String input) {
    if (!ObjectUtils.isEmpty(input)) {
      return input
          .replace("\"", "")
          .replaceFirst("^([\\w+\\s*\\d*\s]+:\\s*)", "")
          .replace("\n", " ")
          .replaceAll("[^A-Za-z0-9\s\'\\.-]", "")
          .trim();
    }
    return input;
  }

  @Override
  public List<String> chunkTranscript(String source, int chunkSize) {
    List<String> tokens = new ArrayList<>(List.of(source.split(" ")));
    List<String> chunks = new ArrayList<>();
    StringJoiner joiner = new StringJoiner(" ");
    int currChunkSize = 0;
    for (String token : tokens) {
      if (currChunkSize >= chunkSize) {
        chunks.add(joiner.toString());
        joiner = new StringJoiner(" ");
        currChunkSize = 0;
      } else {
        joiner.add(token);
        currChunkSize++;
      }
    }
    String chunk = joiner.toString().trim();
    if (!ObjectUtils.isEmpty(chunk)) {
      chunks.add(chunk);
    }
    return chunks;
  }

  @Override
  public String extractGptResponse(List<CompletionChoice> completionChoices) {
    StringJoiner joiner = new StringJoiner(" ");
    // Remove any prefixes separated by a colon
    for (CompletionChoice choice : completionChoices) {
      String text = choice.getText();
      int index = text.indexOf(":");
      if (index > 0 && index+1 < text.length()) {
        text = cleanup(text.substring(index+1));
      }
      joiner.add(text);
    }
    return joiner.toString();
  }

  private void saveContent(Long projectId, List<ContentItem> generatedContent) {
    // Update the completion status
    Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(projectId);
    if (projectEntityOpt.isPresent()) {
      try {
        ProjectEntity entity = projectEntityOpt.get();
        entity.setContent(objectMapper.writeValueAsString(generatedContent));
        entity.setProcessed(true);
        entity.setUpdatedAt(LocalDateTime.now());
        projectRepo.save(entity);
      } catch (JsonProcessingException e) {
        log.error("LanguageServiceImpl.saveContent: {}", e.getLocalizedMessage());
      }
    }
  }

  private void setProjectFailure(long projectId, String reason) {
    Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(projectId);
    if (projectEntityOpt.isPresent()) {
      // Do not set processed=true so that the pending job query can find it.
      // In the future we will add the option to delete failed jobs
        ProjectEntity entity = projectEntityOpt.get();
        entity.setFailed(true);
        entity.setReasonFailed(reason);
        entity.setUpdatedAt(LocalDateTime.now());
        projectRepo.save(entity);
    }
  }

  private static class RecipePartName {
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String INGREDIENTS = "ingredients";
    public static final String INSTRUCTIONS = "instructions";
  }
}
