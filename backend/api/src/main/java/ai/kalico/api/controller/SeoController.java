package ai.kalico.api.controller;

import ai.kalico.api.SeoApi;
import ai.kalico.api.service.seo.SeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Biz Melesse
 * created on 04/06/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SeoController implements SeoApi {
  private final SeoService seoService;

  @Override
  public ResponseEntity<String> getSitemap() {
    return ResponseEntity.ok(seoService.getSitemap());
  }
}
