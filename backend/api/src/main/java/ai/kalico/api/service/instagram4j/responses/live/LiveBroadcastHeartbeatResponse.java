package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastHeartbeatResponse extends IGResponse {
    private int viewer_count;
    private String broadcast_status;
    private String[] cobroadcaster_ids;
    private int offset_to_video_start;
}
