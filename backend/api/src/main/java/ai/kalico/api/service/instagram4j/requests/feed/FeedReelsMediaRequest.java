package ai.kalico.api.service.instagram4j.requests.feed;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedReelsMediaResponse;
import java.util.stream.Stream;
import lombok.Getter;

public class FeedReelsMediaRequest extends IGPostRequest<FeedReelsMediaResponse> {
    private String[] _ids;

    public FeedReelsMediaRequest(Object... ids) {
        _ids = Stream.of(ids).map(Object::toString).toArray(String[]::new);
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String[] user_ids = _ids;
        };
    }

    @Override
    public String path() {
        return "feed/reels_media/";
    }

    @Override
    public Class<FeedReelsMediaResponse> getResponseType() {
        return FeedReelsMediaResponse.class;
    }

}
