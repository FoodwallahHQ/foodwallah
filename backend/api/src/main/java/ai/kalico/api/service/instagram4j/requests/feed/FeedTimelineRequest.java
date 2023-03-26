package ai.kalico.api.service.instagram4j.requests.feed;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPaginatedRequest;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedTimelineResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class FeedTimelineRequest extends IGPostRequest<FeedTimelineResponse>
        implements IGPaginatedRequest {
    @Setter
    private String max_id = "";

    @Override
    public IGPayload getPayload(IGClient client) {
        FeedTimelinePayload payload = new FeedTimelinePayload();
        if (!max_id.isEmpty()) {
            payload.setMax_id(max_id);
            payload.setReason("pagination");
        }
        return payload;
    }

    @Override
    public String path() {
        return "feed/timeline/";
    }

    @Override
    public Class<FeedTimelineResponse> getResponseType() {
        return FeedTimelineResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public static class FeedTimelinePayload extends IGPayload {
        private String max_id;
        private String reason = "cold_start_fetch";
        private String is_pull_to_refresh = "0";
        private String is_prefetch = "0";
    }
}
