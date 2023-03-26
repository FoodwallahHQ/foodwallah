package ai.kalico.api.service.instagram4j.models.media.thread;

import ai.kalico.api.service.instagram4j.models.media.ImageVersions;
import ai.kalico.api.service.instagram4j.models.media.VideoVersionsMeta;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("2")
public class ThreadVideoMedia extends ThreadMedia {
    private ImageVersions image_versions2;
    private List<VideoVersionsMeta> video_versions;
}
