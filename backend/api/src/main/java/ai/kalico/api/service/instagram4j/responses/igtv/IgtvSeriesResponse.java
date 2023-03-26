package ai.kalico.api.service.instagram4j.responses.igtv;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IgtvSeriesResponse extends IGResponse {
    private String id;
    private String title;
    private String description;
}
