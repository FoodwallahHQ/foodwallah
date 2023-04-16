package ai.kalico.api.service.cms;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.service.mapper.RecipeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.GenericResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.RecipeLite;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 4/16/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmsServiceImpl implements CmsService {
  private final RecipeRepo recipeRepo;
  private final RecipeMapper recipeMapper;
  private final ObjectMapper objectMapper;

  @Override
  public PageableRecipeResponse getAllPosts(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size,
        Sort.by("created_at").descending());
    Page<RecipeEntity> pageResult = recipeRepo.cmsFindAllRecipes(pageable);
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
  public RecipeFull getPostById(Long id) {
    Optional<RecipeEntity> entityOpt = recipeRepo.findById(id);
    if (entityOpt.isPresent()) {
      RecipeEntity entity = entityOpt.get();
      return new RecipeFull()
          .source(entity.getCanonicalUrl())
          .summary(entity.getSummary())
          .keywords(entity.getKeywords())
          .ingredients(recipeMapper.mapIngredients(entity.getIngredients(), objectMapper))
          .instructions(recipeMapper.mapRecipeSteps(entity.getInstructions(), objectMapper))
          .recipeLite(recipeMapper.map(entity));
    }
   return null;
  }

  @Override
  public GenericResponse updatePost(RecipeFull recipeFull) {
    Optional<RecipeEntity> entityOpt = recipeRepo.findById(recipeFull.getRecipeLite().getId());
    if (entityOpt.isPresent()) {
      RecipeEntity entity = entityOpt.get();
      recipeMapper.map(recipeFull, entity, objectMapper);
      recipeRepo.save(entity);
    }
    return new GenericResponse().status("OK");
  }


}
