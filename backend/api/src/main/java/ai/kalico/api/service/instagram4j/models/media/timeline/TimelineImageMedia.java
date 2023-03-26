package ai.kalico.api.service.instagram4j.models.media.timeline;

import ai.kalico.api.service.instagram4j.models.media.ImageMedia;
import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import ai.kalico.api.service.instagram4j.models.media.ImageVersionsMeta;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("1")
public class TimelineImageMedia extends TimelineMedia implements ImageMedia {
    private ImageVersions image_versions2;
    private List<ImageVersionsMeta> candidates;
    private long video_duration;
    private boolean has_audio;
    private int original_width;
    private int original_height;
    private int view_count;
}
