package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveStartResponse extends IGResponse {
    private String media_id;
}
