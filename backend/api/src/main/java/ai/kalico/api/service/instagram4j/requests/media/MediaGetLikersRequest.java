package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedUsersResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaGetLikersRequest extends IGGetRequest<FeedUsersResponse> {
    @NonNull
    private String _id;

    @Override
    public String path() {
        return "media/" + _id + "/likers/";
    }

    @Override
    public Class<FeedUsersResponse> getResponseType() {
        return FeedUsersResponse.class;
    }
}
