package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.media.thread.ThreadMedia;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("raven_media")
public class ThreadRavenMediaItem extends ThreadItem {
    private ThreadVisualMedia visual_media;

    @Getter @Setter
    public static class ThreadVisualMedia {
        private long url_expire_at_secs;
        private int playback_duration_secs;
        private ThreadMedia media;
        private List<String> seen_user_ids;
        private String view_mode;
        private int seen_count;
    }
}
