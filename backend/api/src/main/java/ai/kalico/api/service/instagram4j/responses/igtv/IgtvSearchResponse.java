package ai.kalico.api.service.instagram4j.responses.igtv;

import ai.kalico.api.service.instagram4j.models.igtv.Channel;
import ai.kalico.api.service.instagram4j.models.user.User;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IgtvSearchResponse extends IGResponse {
    private List<IgtvSearchResult> results;
    private int num_results;
    private boolean has_more;
    private String rank_token;

    @Getter @Setter
    public static class IgtvSearchResult {
        private String type;
        private User user;
        private Channel channel;
    }
}
