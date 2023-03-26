package ai.kalico.api.service.instagram4j.requests.feed;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.requests.IGPaginatedRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedTagResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class FeedTagRequest extends IGGetRequest<FeedTagResponse>
        implements IGPaginatedRequest {
    @NonNull
    private String tag;
    @Setter
    private String max_id;

    @Override
    public String path() {
        return "feed/tag/" + tag + "/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id);
    }

    @Override
    public Class<FeedTagResponse> getResponseType() {
        return FeedTagResponse.class;
    }

}
