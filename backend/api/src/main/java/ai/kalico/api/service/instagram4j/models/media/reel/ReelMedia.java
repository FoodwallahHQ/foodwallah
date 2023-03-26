package ai.kalico.api.service.instagram4j.models.media.reel;

import ai.kalico.api.service.instagram4j.models.media.Media;
import ai.kalico.api.service.instagram4j.models.media.Viewer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(defaultImpl = ReelMedia.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReelImageMedia.class),
        @JsonSubTypes.Type(value = ReelVideoMedia.class)
})
public class ReelMedia extends Media {
    private int viewer_count;
    private int total_viewer_count;
    private List<Viewer> viewers;
}
