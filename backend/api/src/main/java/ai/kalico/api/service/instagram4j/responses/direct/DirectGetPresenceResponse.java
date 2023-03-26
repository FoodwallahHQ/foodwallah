package ai.kalico.api.service.instagram4j.responses.direct;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DirectGetPresenceResponse extends IGResponse {
    private Map<Long, UserPresence> user_presence;

    @Getter @Setter
    public static class UserPresence {
        @JsonProperty("is_active")
        private boolean is_active;
        private long last_activity_at_ms;
    }
}
