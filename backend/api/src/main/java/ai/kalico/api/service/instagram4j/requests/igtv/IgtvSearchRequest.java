package ai.kalico.api.service.instagram4j.requests.igtv;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.igtv.IgtvSearchResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IgtvSearchRequest extends IGPostRequest<IgtvSearchResponse> {
    private String _query;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvSearchPayload();
    }

    @Override
    public String path() {
        return String.format("igtv/%s/", _query != null ? "search" : "suggested_searches");
    }

    @Override
    public Class<IgtvSearchResponse> getResponseType() {
        return IgtvSearchResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public class IgtvSearchPayload extends IGPayload {
        private String query = _query;
    }

}
