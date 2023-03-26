package ai.kalico.api.service.instagram4j.requests.direct;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

public class DirectThreadsApproveRequest extends IGPostRequest<IGResponse> {
    private final String[] _thread_ids;

    public DirectThreadsApproveRequest(String... thread_ids) {
        this._thread_ids = thread_ids;
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new DirectThreadsApproveRequestPayload();
    }

    @Override
    public String path() {
        return String.format("direct_v2/threads/%s/",
                _thread_ids.length > 1 ? "approve_multiple" : (_thread_ids[0] + "/approve"));
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    public class DirectThreadsApproveRequestPayload extends IGPayload {
        private final String[] thread_ids = _thread_ids.length > 1 ? _thread_ids : null;
    }
}
