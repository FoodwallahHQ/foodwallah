package ai.kalico.api.service.instagram4j.responses.igtv;

import ai.kalico.api.service.instagram4j.models.igtv.Channel;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IgtvBrowseFeedResponse extends IGResponse implements IGPaginatedResponse {
    private Channel my_channel;
    private List<Channel> channels;
    @JsonProperty("max_id")
    private String next_max_id;
    private boolean more_available;
}
