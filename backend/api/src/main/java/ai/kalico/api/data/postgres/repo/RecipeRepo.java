package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.projection.RecipeMetaProjection;
import ai.kalico.api.data.postgres.projection.UserProjectProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Bizuwork Melesse
 * created on March 24, 2023
 */
@Repository
@Transactional
public interface RecipeRepo extends JpaRepository<RecipeEntity, Long> {

  Optional<RecipeEntity> findBySlug(String slug);
  Optional<RecipeEntity> findByContentId(String contentId);
  @Query(value = "SELECT * FROM public.recipe WHERE processed = true",
      nativeQuery = true)
  Page<RecipeEntity> findAllRecipes(Pageable pageable);

  @Query(value = "SELECT slug, title, updated_at as updatedAt "
      + "FROM public.recipe "
      + "ORDER BY updated_at ASC",
      nativeQuery = true)
  List<RecipeMetaProjection> findAllSlugs();

  @Query(value = "SELECT * "
      + "FROM public.recipe WHERE created_at <= ?1 AND id != ?2 "
      + "ORDER BY created_at DESC "
      + "LIMIT 1",
      nativeQuery = true)
  RecipeEntity findPrev(LocalDateTime createdAt, Long id);

  @Query(value = "SELECT * "
      + "FROM public.recipe WHERE created_at >= ?1 AND id != ?2 "
      + "ORDER BY created_at ASC "
      + "LIMIT 1",
      nativeQuery = true)
  RecipeEntity findNext(LocalDateTime createdAt, Long id);
}
