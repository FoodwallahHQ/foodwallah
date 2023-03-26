package ai.kalico.api.service.language;

import ai.kalico.api.dto.GptResponse;
import ai.kalico.api.dto.RecipeGptResponse;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import com.kalico.model.ContentItem;
import java.util.List;

/**
 * @author Biz Melesse created on 1/30/23
 */
public interface LanguageService {

  /**
   * Apply GPT-3 or equivalent ML model/s to generate the content from raw
   * data gathered so far.
   * @param projectId
   * @return the generated content formatted for rendering in the UI
   */
  List<ContentItem> generateContent(Long projectId, String mediaId, boolean recipeContent);

  /**
   * Cleanup text by extracting texts and number without any special characters
   *
   * @param input the text to clean up
   * @return
   */
  String cleanup(String input);

  /**
   * Break input text into chunks so we can remain below OpenAI's token limit
   *
   * @param input raw transcript or scraped description
   * @return
   */
  List<String> chunkTranscript(String input, int chunkSize);

  /**
   * Extract the content part of the GPT completion response by dropping the prefix
   * @param completionChoices
   * @return
   */
  String extractGptResponse(List<CompletionChoice> completionChoices);

  RecipeGptResponse extractRecipeGptResponse(GptResponse recipeGptCompletion);

}
