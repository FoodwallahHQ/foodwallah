package ai.kalico.api.service.recipe;

import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.StringDto;

/**
 * @author Biz Melesse created on 3/24/23
 */
public interface RecipeService {
  CreateRecipeResponse createRecipe(StringDto stringDto);
  PageableRecipeResponse getAllRecipes(Integer page, Integer size);
  RecipeFull getFullRecipe(String slug);
  PageableRecipeResponse getMostRecentRecipes(Integer page, Integer size);

  CreateRecipeResponse regenerateRecipe(StringDto stringDto);
}
