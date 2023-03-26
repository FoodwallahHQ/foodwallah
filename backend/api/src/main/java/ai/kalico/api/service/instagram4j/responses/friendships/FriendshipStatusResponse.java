package ai.kalico.api.service.instagram4j.responses.friendships;

import ai.kalico.api.service.instagram4j.models.friendships.Friendship;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FriendshipStatusResponse extends IGResponse {
    private Friendship friendship_status;
}
