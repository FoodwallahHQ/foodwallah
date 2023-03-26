package ai.kalico.api.service.instagram4j.responses.highlights;

import ai.kalico.api.service.instagram4j.models.feed.Reel;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HighlightsCreateReelResponse extends IGResponse {
    private Reel reel;
}
