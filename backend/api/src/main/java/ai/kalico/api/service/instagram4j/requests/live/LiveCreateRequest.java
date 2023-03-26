package ai.kalico.api.service.instagram4j.requests.live;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.live.LiveCreateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LiveCreateRequest extends IGPostRequest<LiveCreateResponse> {
    private int width = 720, height = 1280;
    private String broadcastMessage = "test", broadcastType = "RTMP";

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new LiveCreatePayload(width, height, broadcastMessage, broadcastType);
    }

    @Override
    public String path() {
        return "live/create/";
    }

    @Override
    public Class<LiveCreateResponse> getResponseType() {
        return LiveCreateResponse.class;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class LiveCreatePayload extends IGPayload {
        private int preview_width;
        private int preview_height;
        private String broadcast_message;
        private String broadcast_type;
    }

}
