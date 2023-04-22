package ai.kalico.api.service.recipe;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.projection.UserProjectProjection;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.dto.AdditionalInfoDto;
import ai.kalico.api.dto.VideoInfoDto;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.service.av.AVService;
import ai.kalico.api.service.language.LanguageService;
import ai.kalico.api.service.mapper.RecipeMapper;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.utils.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ContentItem;
import com.kalico.model.ContentPreviewResponse;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.RecipeLite;
import com.kalico.model.StringDto;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 3/24/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
  private final RecipeRepo recipeRepo;
  private final ObjectMapper objectMapper;
  private final AVService avService;
  private final AWSProps awsProps;
  private final LanguageService languageService;
  private final RecipeMapper recipeMapper;
  
  @Override
  public CreateRecipeResponse createRecipe(StringDto stringDto) {
    var url = stringDto.getValue();
    if (ObjectUtils.isEmpty(url)) {
      return new CreateRecipeResponse().error("URL must not be empty");
    }
    url = KALUtils.normalizeUrl(url);
    if (KALUtils.getPlatform(url) == Platform.INVALID) {
      return new CreateRecipeResponse().error("Please try again with a YouTube link");
    }
    VideoInfoDto dto = avService.getContent(url);
    if (dto == null) {
      return new CreateRecipeResponse().error("Unable to process the url provided");
    }

    var canonicalUrl = dto.getPermalink();
    var contentId = dto.getMediaIdOverride();
    if (dto.getVideoInfo() != null && dto.getVideoInfo().details() != null) {
      contentId = dto.getVideoInfo().details().videoId();
      canonicalUrl = "https://www.youtube.com/watch?v=" + contentId;
    }
    if (contentId != null) {
      var inProgressMsg = "We're currently processing this url. Please check again in a few minutes.";
      Optional<RecipeEntity> entityOpt = recipeRepo.findByContentId(contentId);
      if (entityOpt.isPresent()) {
        RecipeEntity entity = entityOpt.get();
        if (entity.getProcessed() && entity.getFailed()) {
          // Failed to process
          return new CreateRecipeResponse().error(entity.getReasonFailed());
        } else if (entity.getProcessed() && entity.getSlug() != null) {
          // Successfully processed
          return new CreateRecipeResponse().slug(entity.getSlug()).status("OK");
        } else {
          // In progress
          return new CreateRecipeResponse().status(inProgressMsg);
        }
      } else {
        ContentPreviewResponse preview = avService.parseContentMetadata(dto);
        AdditionalInfoDto additionalInfoDto = new AdditionalInfoDto();
        additionalInfoDto.setCookingTime(preview.getDurationMinutes());
        RecipeEntity entity = new RecipeEntity();
        entity.setContentId(contentId);
        entity.setCanonicalUrl(canonicalUrl);
        entity.setThumbnail(String.format("%s/%s/%s",
            awsProps.getCdn(),
            awsProps.getImageFolder(),
            contentId));
        recipeRepo.save(entity);
        avService.processRecipeContent(canonicalUrl, dto, contentId, preview.getThumbnail());
        return new CreateRecipeResponse().status(inProgressMsg);
      }
    }
    log.warn("Failed to process url because contentId is not found: {}", url);
    // Should never reach here
    return new CreateRecipeResponse().error("Unable to process this url");
  }

  @Override
  public PageableRecipeResponse getAllRecipes(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size,
        Sort.by("created_at").descending());
    Page<RecipeEntity> pageResult = recipeRepo.findAllRecipes(pageable);
    PageableRecipeResponse response = new PageableRecipeResponse();
    response.setTotalRecords(pageResult.getTotalElements());
    response.setNumPages(pageResult.getTotalPages());
    response.setRecords(pageResult
        .getContent()
        .stream()
        .map(recipeMapper::map)
        .collect(Collectors.toList()));
    return response;
  }

  @Override
  public RecipeFull getFullRecipe(String slug) {
    Optional<RecipeEntity> entityOpt = recipeRepo.findBySlug(slug);
    if (entityOpt.isPresent()) {
      RecipeEntity entity = entityOpt.get();
      RecipeEntity prev = recipeRepo.findPrev(entity.getCreatedAt(), entity.getId());
      RecipeEntity next = recipeRepo.findNext(entity.getCreatedAt(), entity.getId());
      return new RecipeFull()
          .source(entity.getCanonicalUrl())
          .summary(entity.getSummary())
          .keywords(entity.getKeywords())
          .ingredients(recipeMapper.mapIngredients(entity.getIngredients(), objectMapper))
          .instructions(recipeMapper.mapRecipeSteps(entity.getInstructions(), objectMapper))
          .recipeLite(recipeMapper.map(entity))
          .prev(recipeMapper.map(prev))
          .next(recipeMapper.map(next));
    }
    return null;
  }



  @Override
  public PageableRecipeResponse getMostRecentRecipes(Integer page, Integer size) {
    return getAllRecipes(page, size);
  }

  @Override
  public CreateRecipeResponse regenerateRecipe(StringDto stringDto) {
    Optional<RecipeEntity> recipeEntityOpt = recipeRepo.findBySlug(stringDto.getValue());
    recipeEntityOpt.ifPresent(
        entity -> languageService.generateContent(null, entity.getContentId(), true));
    return new CreateRecipeResponse().status("Regeneration in progress");
  }

//  private RecipeLite getLiteRecipe(RecipeEntity entity) {
//    if (entity == null) {
//      return null;
//    }
//    return new RecipeLite()
//        .cookingTime(entity.getCookingTimeMinutes())
//        .summary(entity.getSummary())
//        .slug(entity.getSlug())
//        .numIngredients(entity.getNumIngredients())
//        .numSteps(entity.getNumSteps())
//        .title(entity.getTitle())
//        .thumbnail(entity.getThumbnail())
//        .createdAt(entity
//            .getCreatedAt()
//            .toEpochSecond(ZoneOffset.UTC));
//  }

//  private List<String> toStringList(Object[] objects) {
//    return Arrays
//        .stream(objects)
//        .map(it -> (String) it)
//        .collect(Collectors.toList());
//  }
}
