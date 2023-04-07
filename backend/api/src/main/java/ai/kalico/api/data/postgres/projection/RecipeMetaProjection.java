package ai.kalico.api.data.postgres.projection;

import java.time.LocalDateTime;

/**
 * @author Biz Melesse created on 4/6/23
 */
public interface RecipeMetaProjection {
  LocalDateTime getUpdatedAt();
  String getSlug();
  String getTitle();
}
