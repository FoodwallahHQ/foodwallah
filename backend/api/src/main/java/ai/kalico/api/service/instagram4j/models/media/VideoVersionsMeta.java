package ai.kalico.api.service.instagram4j.models.media;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VideoVersionsMeta implements Meta {
    private int height;
    private int width;
    private String id;
    private String type;
    private String url;
}
