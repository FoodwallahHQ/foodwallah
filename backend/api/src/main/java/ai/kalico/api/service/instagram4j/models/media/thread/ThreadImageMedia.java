package ai.kalico.api.service.instagram4j.models.media.thread;

import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("1")
public class ThreadImageMedia extends ThreadMedia {
    private ImageVersions image_versions2;
}
