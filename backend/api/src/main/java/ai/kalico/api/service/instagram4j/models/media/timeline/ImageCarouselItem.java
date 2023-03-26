package ai.kalico.api.service.instagram4j.models.media.timeline;

import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

// media_type 1
@Getter @Setter
@JsonTypeName("1")
public class ImageCarouselItem extends CarouselItem {
    private ImageVersions image_versions2;
}
