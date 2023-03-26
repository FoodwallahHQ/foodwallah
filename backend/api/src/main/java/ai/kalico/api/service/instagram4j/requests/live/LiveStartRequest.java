package ai.kalico.api.service.instagram4j.requests.live;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.live.LiveStartResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LiveStartRequest extends IGPostRequest<LiveStartResponse> {
    private String broadcastId;
    private boolean sendNotification;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private boolean should_send_notifications = sendNotification;
        };
    }

    @Override
    public String path() {
        return "live/" + broadcastId + "/start/";
    }

    @Override
    public Class<LiveStartResponse> getResponseType() {
        return LiveStartResponse.class;
    }

}
