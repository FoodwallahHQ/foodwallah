package ai.kalico.api.service.instagram4j.models.media;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageVersions {
    private List<ImageVersionsMeta> candidates;
}
