package ai.kalico.api.service.seo;

import ai.kalico.api.data.postgres.projection.RecipeMetaProjection;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.props.ProjectProps;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 4/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeoServiceImpl implements SeoService {
  private final RecipeRepo recipeRepo;
  private final ProjectProps projectProps;

  @Override
  public String getSitemap() {
    List<RecipeMetaProjection> entities = recipeRepo.findAllSlugs();
    List<String> urls = new ArrayList<>();
    for (RecipeMetaProjection entity : entities) {
      urls.add(String.format("<url>\n"
          + "    <loc>%s</loc>\n"
          + "    <lastmod>%s</lastmod>\n"
          + "  </url>\n",
          projectProps.getSiteUrl() + "/" + entity.getSlug(),
          entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
    return String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
        + "%s"
        + "</urlset>", String.join("\n", urls));
  }
}
