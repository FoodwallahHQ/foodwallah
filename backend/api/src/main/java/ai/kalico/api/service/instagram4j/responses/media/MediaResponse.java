package ai.kalico.api.service.instagram4j.responses.media;

import ai.kalico.api.service.instagram4j.models.media.Media;
import ai.kalico.api.service.instagram4j.models.media.reel.ReelMedia;
import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineVideoMedia;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaResponse extends IGResponse {
    private Media media;

    @Getter @Setter
    public static class MediaConfigureTimelineResponse extends MediaResponse {
        private TimelineMedia media;
    }

    @Getter @Setter
    public static class MediaConfigureSidecarResponse extends MediaConfigureTimelineResponse {
        private String client_sidecar_id;
    }

    @Getter @Setter
    public static class MediaConfigureToStoryResponse extends MediaResponse {
        private ReelMedia media;
    }

    @Getter @Setter
    public static class MediaConfigureToIgtvResponse extends MediaResponse {
        private TimelineVideoMedia media;
    }
}
