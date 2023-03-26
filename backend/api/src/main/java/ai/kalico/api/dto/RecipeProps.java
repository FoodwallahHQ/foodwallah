package ai.kalico.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Biz Melesse created on 3/25/23
 */
@Getter
@Setter
public class RecipeProps {
  private String title;
  private String summary;
  private String description;
  private String instructions;
  private String ingredients;
  private int chunkSize;
  private int maxTokens;
  private double temp;
  private String consolidatedPrompt;
}
