package ai.kalico.api.controller;

import ai.kalico.api.RecipeApi;
import ai.kalico.api.service.recipe.RecipeService;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.StringDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Biz Melesse
 * created on 03/24/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {
  private final RecipeService recipeService;

  @Override
  public ResponseEntity<CreateRecipeResponse> createRecipe(StringDto stringDto) {
    return ResponseEntity.ok(recipeService.createRecipe(stringDto));
  }

  @Override
  public ResponseEntity<PageableRecipeResponse> getAllRecipes(Integer page, Integer size) {
    return ResponseEntity.ok(recipeService.getAllRecipes(page, size));
  }

  @Override
  public ResponseEntity<RecipeFull> getFullRecipe(String slug) {
    return ResponseEntity.ok(recipeService.getFullRecipe(slug));
  }

  @Override
  public ResponseEntity<PageableRecipeResponse> getMostRecentRecipes(Integer page, Integer size) {
    return ResponseEntity.ok(recipeService.getMostRecentRecipes(page, size));
  }

  @Override
  public ResponseEntity<CreateRecipeResponse> regenerateRecipe(StringDto stringDto) {
    return ResponseEntity.ok(recipeService.regenerateRecipe(stringDto));
  }
}
