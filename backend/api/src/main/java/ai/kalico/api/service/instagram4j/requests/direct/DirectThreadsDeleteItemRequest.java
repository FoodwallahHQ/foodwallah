package ai.kalico.api.service.instagram4j.requests.direct;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectThreadsDeleteItemRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String _thread_id;
    @NonNull
    private String _item_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return String.format("direct_v2/threads/%s/items/%s/delete/", _thread_id, _item_id);
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
