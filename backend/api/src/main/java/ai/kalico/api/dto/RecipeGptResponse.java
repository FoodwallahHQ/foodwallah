package ai.kalico.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Biz Melesse created on 3/26/23
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RecipeGptResponse {
  private String title;
  private String summary;
  private List<String> ingredients;
  private List<String> instructions;
}