package ai.kalico.api.service.instagram4j.responses.users;

import ai.kalico.api.service.instagram4j.models.friendships.Friendship;
import ai.kalico.api.service.instagram4j.models.user.Profile;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsersSearchResponse extends IGResponse {

    private int num_results;
    private List<User> users;
    private boolean has_more;
    private String rank_token;
    private String page_token;

    @Getter @Setter
    public static class User extends Profile {
        Friendship friendship_status;
        String social_context;
        String search_social_context;
        int mutual_followers_count;
        int latest_reel_media;
    }

}
