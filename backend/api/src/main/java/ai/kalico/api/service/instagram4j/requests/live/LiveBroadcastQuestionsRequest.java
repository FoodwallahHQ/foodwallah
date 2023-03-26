package ai.kalico.api.service.instagram4j.requests.live;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveBroadcastQuestionsRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String _broadcast_id, _text;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String text = _text;
        };
    }

    @Override
    public String path() {
        return "live/" + _broadcast_id + "/questions/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
