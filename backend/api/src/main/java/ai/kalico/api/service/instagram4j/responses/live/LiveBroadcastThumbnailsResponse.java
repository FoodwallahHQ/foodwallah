package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastThumbnailsResponse extends IGResponse {
    private List<String> thumbnails;
}
