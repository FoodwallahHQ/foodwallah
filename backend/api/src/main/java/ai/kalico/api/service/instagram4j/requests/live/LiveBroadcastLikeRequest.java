package ai.kalico.api.service.instagram4j.requests.live;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.live.LiveBroadcastLikeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class LiveBroadcastLikeRequest extends IGPostRequest<LiveBroadcastLikeResponse> {
    @NonNull
    private String broadcast_id;
    private int count = 1;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private int user_like_count = count;
        };
    }

    @Override
    public String path() {
        return "live/" + broadcast_id + "/like/";
    }

    @Override
    public Class<LiveBroadcastLikeResponse> getResponseType() {
        return LiveBroadcastLikeResponse.class;
    }

}
