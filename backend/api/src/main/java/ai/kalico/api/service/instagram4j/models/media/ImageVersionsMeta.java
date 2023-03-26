package ai.kalico.api.service.instagram4j.models.media;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageVersionsMeta implements Meta {
    private String url;
    private int width;
    private int height;
}
