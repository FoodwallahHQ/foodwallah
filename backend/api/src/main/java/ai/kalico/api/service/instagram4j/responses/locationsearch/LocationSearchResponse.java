package ai.kalico.api.service.instagram4j.responses.locationsearch;

import ai.kalico.api.service.instagram4j.models.location.Location.Venue;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationSearchResponse extends IGResponse {
    private List<Venue> venues;
    private String request_id;
    private String rank_token;
}
