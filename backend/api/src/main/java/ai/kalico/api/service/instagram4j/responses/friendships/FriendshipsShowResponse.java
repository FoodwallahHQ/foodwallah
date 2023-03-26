package ai.kalico.api.service.instagram4j.responses.friendships;

import ai.kalico.api.service.instagram4j.models.friendships.Friendship;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FriendshipsShowResponse extends IGResponse {
    @JsonUnwrapped
    private Friendship friendship;
}
