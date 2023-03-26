package ai.kalico.api.service.instagram4j.models.media.reel;

import ai.kalico.api.service.instagram4j.models.media.ImageMedia;
import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("1")
public class ReelImageMedia extends ReelMedia implements ImageMedia {
    private ImageVersions image_versions2;
}
