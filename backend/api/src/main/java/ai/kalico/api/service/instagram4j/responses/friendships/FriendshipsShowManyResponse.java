package ai.kalico.api.service.instagram4j.responses.friendships;

import ai.kalico.api.service.instagram4j.models.friendships.Friendship;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FriendshipsShowManyResponse extends IGResponse {
    private Map<String, Friendship> friendship_statuses;
}
