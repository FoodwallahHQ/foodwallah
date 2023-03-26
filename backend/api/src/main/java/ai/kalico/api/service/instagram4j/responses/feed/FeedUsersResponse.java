package ai.kalico.api.service.instagram4j.responses.feed;

import ai.kalico.api.service.instagram4j.models.user.Profile;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedUsersResponse extends IGResponse implements IGPaginatedResponse {
    private List<Profile> users;
    private String next_max_id;

    public boolean isMore_available() {
        return next_max_id != null;
    }
}
