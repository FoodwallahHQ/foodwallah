package ai.kalico.api.service.instagram4j.responses.feed;

import ai.kalico.api.service.instagram4j.models.feed.Reel;
import ai.kalico.api.service.instagram4j.models.live.Broadcast;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedUserStoryResponse extends IGResponse {
    private Reel reel;
    private Broadcast broadcast;
}
