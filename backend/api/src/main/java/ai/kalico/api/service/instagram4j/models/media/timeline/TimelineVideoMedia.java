package ai.kalico.api.service.instagram4j.models.media.timeline;

import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import ai.kalico.api.service.instagram4j.models.media.VideoVersionsMeta;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// media_type 2
@Getter @Setter
@JsonTypeName("2")
public class TimelineVideoMedia extends TimelineMedia {
    private List<VideoVersionsMeta> video_versions;
    private ImageVersions image_versions2;
    private long video_duration;
    private boolean has_audio;
    private int original_width;
    private int original_height;
    private int view_count;
}
