package ai.kalico.api.service.cms;

import com.kalico.model.GenericResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;

/**
 * @author Biz Melesse created on 4/16/23
 */
public interface CmsService {
  PageableRecipeResponse getAllPosts(Integer page, Integer size);

  RecipeFull getPostById(Long id);

  GenericResponse updatePost(RecipeFull recipeFull);
}
