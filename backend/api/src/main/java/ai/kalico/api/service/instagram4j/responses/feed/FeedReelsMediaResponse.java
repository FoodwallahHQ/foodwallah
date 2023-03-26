package ai.kalico.api.service.instagram4j.responses.feed;

import ai.kalico.api.service.instagram4j.models.feed.Reel;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedReelsMediaResponse extends IGResponse {
    @JsonUnwrapped
    private Map<String, Reel> reels = new HashMap<String, Reel>();
}
