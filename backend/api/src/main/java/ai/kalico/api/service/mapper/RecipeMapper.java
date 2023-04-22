package ai.kalico.api.service.mapper;


import ai.kalico.api.data.postgres.entity.RecipeEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.Ingredient;
import com.kalico.model.RecipeFull;
import com.kalico.model.RecipeLite;
import com.kalico.model.RecipeStep;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * @author Biz Melesse
 * created on 4/16/23
 */


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {JsonNullableMapper.class})
public interface RecipeMapper {

  default List<Ingredient> mapIngredients(String value, ObjectMapper objectMapper) {
    TypeReference<List<Ingredient>> typeRef = new TypeReference<>() {};
    try {
      return objectMapper.readValue(value, typeRef);
    } catch (JsonProcessingException e) {
      return stringToList(value, objectMapper)
          .stream()
          .map(it -> new Ingredient()
              .ingredient(it))
          .collect(Collectors.toList());
    }
  }

  default List<RecipeStep> mapRecipeSteps(String value, ObjectMapper objectMapper) {
    TypeReference<List<RecipeStep>> typeRef = new TypeReference<>() {};
    try {
      return objectMapper.readValue(value, typeRef);
    } catch (JsonProcessingException e) {
      return stringToList(value, objectMapper)
          .stream()
          .map(it -> new RecipeStep()
              .text(it)
              .stepNumber(0))
         .collect(Collectors.toList());
    }
  }

  default String mapFromRecipeSteps(List<RecipeStep> steps, ObjectMapper objectMapper) {
    try {
      return objectMapper.writeValueAsString(steps);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "{}";
  }

  default String mapFromIngredients(List<Ingredient> steps, ObjectMapper objectMapper) {
    try {
      return objectMapper.writeValueAsString(steps);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "{}";
  }

  default void map(RecipeFull recipeFull, RecipeEntity recipeEntity, ObjectMapper objectMapper) {
    recipeEntity.setInstructions(mapFromRecipeSteps(recipeFull.getInstructions(), objectMapper));
    recipeEntity.setIngredients(mapFromIngredients(recipeFull.getIngredients(), objectMapper));
    recipeEntity.setTitle(recipeFull.getRecipeLite().getTitle());
    recipeEntity.setSummary(recipeFull.getSummary());
    recipeEntity.setNumSteps(recipeFull.getRecipeLite().getNumSteps());
    recipeEntity.setNumIngredients(recipeFull.getRecipeLite().getNumIngredients());
    recipeEntity.setPublished(recipeFull.getRecipeLite().getPublished());
    recipeEntity.setDescription(recipeFull.getRecipeLite().getDescription());
    recipeEntity.setAdditionalInfo(recipeFull.getRecipeLite().getAdditionalInfo());
    recipeEntity.setKeywords(recipeFull.getKeywords());
    recipeEntity.setThumbnail(recipeFull.getRecipeLite().getThumbnail());
    recipeEntity.setUpdatedAt(LocalDateTime.now());
  }

  default RecipeLite map(RecipeEntity entity) {
    if (entity == null) {
      return null;
    }
    return new RecipeLite()
        .id(entity.getId())
        .published(entity.getPublished())
        .additionalInfo(entity.getAdditionalInfo())
        .description(entity.getDescription())
        .slug(entity.getSlug())
        .numIngredients(entity.getNumIngredients())
        .numSteps(entity.getNumSteps())
        .title(entity.getTitle())
        .thumbnail(entity.getThumbnail())
        .createdAt(entity
            .getCreatedAt()
            .toEpochSecond(ZoneOffset.UTC));

  }

  List<RecipeLite> map(List<RecipeEntity> recipeEntities);

  default List<String> stringToList(String s, ObjectMapper objectMapper) {
    TypeReference<List<String>> typeRef = new TypeReference<>() {};
    try {
      return objectMapper.readValue(s, typeRef);
    } catch (JsonProcessingException e) {
     e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
