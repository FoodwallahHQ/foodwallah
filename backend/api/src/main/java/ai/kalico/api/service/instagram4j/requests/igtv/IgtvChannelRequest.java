package ai.kalico.api.service.instagram4j.requests.igtv;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPaginatedRequest;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.igtv.IgtvChannelResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class IgtvChannelRequest extends IGPostRequest<IgtvChannelResponse>
        implements IGPaginatedRequest {
    @NonNull
    private String _id;
    @Setter
    private String max_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvChannelPayload();
    }

    @Override
    public String path() {
        return "igtv/channel/";
    }

    @Override
    public Class<IgtvChannelResponse> getResponseType() {
        return IgtvChannelResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public class IgtvChannelPayload extends IGPayload {
        private String id = _id;
        @JsonProperty("max_id")
        private String _max_id = max_id;
    }

}
