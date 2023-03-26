package ai.kalico.api.service.instagram4j.requests.friendships;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.friendships.FriendshipsShowResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendshipsShowRequest extends IGGetRequest<FriendshipsShowResponse> {
    @NonNull
    private Long pk;

    @Override
    public String path() {
        return "friendships/show/" + pk + "/";
    }

    @Override
    public Class<FriendshipsShowResponse> getResponseType() {
        return FriendshipsShowResponse.class;
    }

}
