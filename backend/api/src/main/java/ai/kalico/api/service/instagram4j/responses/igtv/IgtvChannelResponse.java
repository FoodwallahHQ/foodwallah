package ai.kalico.api.service.instagram4j.responses.igtv;

import ai.kalico.api.service.instagram4j.models.igtv.Channel;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IgtvChannelResponse extends IGResponse implements IGPaginatedResponse {
    @JsonUnwrapped
    private Channel channel;
    @JsonProperty("max_id")
    private String next_max_id;
    private boolean more_available;
}
