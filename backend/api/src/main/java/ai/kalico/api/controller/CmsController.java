package ai.kalico.api.controller;

import ai.kalico.api.CmsApi;
import ai.kalico.api.service.cms.CmsService;
import com.kalico.model.GenericResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Biz Melesse created on 4/16/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CmsController implements CmsApi {
  private final CmsService cmsService;

  @Override
  public ResponseEntity<PageableRecipeResponse> getAllPosts(Integer page, Integer size) {
    return ResponseEntity.ok(cmsService.getAllPosts(page, size));
  }

  @Override
  public ResponseEntity<RecipeFull> getPostById(Long id) {
    return ResponseEntity.ok(cmsService.getPostById(id));
  }

  @Override
  public ResponseEntity<GenericResponse> updatePost(RecipeFull recipeFull) {
    return ResponseEntity.ok(cmsService.updatePost(recipeFull));
  }
}
