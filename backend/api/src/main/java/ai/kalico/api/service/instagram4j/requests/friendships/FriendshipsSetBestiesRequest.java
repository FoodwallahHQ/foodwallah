package ai.kalico.api.service.instagram4j.requests.friendships;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.friendships.FriendshipStatusResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendshipsSetBestiesRequest extends IGPostRequest<FriendshipStatusResponse> {
    private final Long[] _add, _remove;

    public FriendshipsSetBestiesRequest(boolean add, Long... pks) {
        this._add = add ? pks : null;
        this._remove = !add ? pks : null;
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new FriendshipsSetBestiesPayload();
    }

    @Override
    public String path() {
        return "friendships/set_besties/";
    }

    @Override
    public Class<FriendshipStatusResponse> getResponseType() {
        return FriendshipStatusResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public class FriendshipsSetBestiesPayload extends IGPayload {
        private Long[] add = _add;
        private Long[] remove = _remove;
    }

}
