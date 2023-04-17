package ai.kalico.api.service.cms;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.service.aws.S3Service;
import ai.kalico.api.service.mapper.RecipeMapper;
import ai.kalico.api.service.utils.KALUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.GenericResponse;
import com.kalico.model.ImageUploadRequest;
import com.kalico.model.ImageUploadResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.RecipeLite;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
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
import org.springframework.util.ObjectUtils;

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
  private final AWSProps awsProps;
  private final S3Service s3Service;

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

  @Override
  public ImageUploadResponse uploadImage(ImageUploadRequest imageUploadRequest) {
    if (imageUploadRequest.getFile().isPresent()) {
      String rawFile = imageUploadRequest.getFile().get();
      String delimiter = "base64,";
      String[] tokens = imageUploadRequest.getFile().get().split(delimiter);
      if (tokens.length > 1 && !ObjectUtils.isEmpty(tokens[1])) {
        rawFile = tokens[1];
      }
      try {
        var bs = Base64.getDecoder().decode(rawFile);
        InputStream in = new ByteArrayInputStream(bs);

        String key = awsProps.getImageFolder() + "/" + KALUtils.generateUid();
        s3Service.uploadImage(awsProps.getBucket(), key, in, S3Service.IMAGE_TYPE);
        return new ImageUploadResponse().url(awsProps.getCdn() + "/" + key);
      } catch (Exception e) {
        log.error("Error in AVServiceImpl.saveFile: {}", e.getLocalizedMessage());
      }
    }
    return new ImageUploadResponse().error("Please provide a supported file to upload");
  }
}
