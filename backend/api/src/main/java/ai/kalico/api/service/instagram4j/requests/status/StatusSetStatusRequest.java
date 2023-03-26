package ai.kalico.api.service.instagram4j.requests.status;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
public class StatusSetStatusRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private StatusSetStatusRequest.StatusSetStatusPayload payload;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return payload;
    }

    @Override
    public String path() {
        return "status/set_status/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Getter @Setter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class StatusSetStatusPayload extends IGPayload {
        @NonNull
        @JsonProperty("text")
        private String text;
        @NonNull
        @JsonProperty("emoji")
        private String emoji;

        @JsonProperty("expires_at")
        private long expires_at;

        @JsonProperty("should_notify")
        private boolean should_notify;
        @NonNull
        @JsonProperty("status_type")
        private String status_type;
    }

    public StatusSetStatusRequest(String text,String emoji,long expires_at)
    {
        payload = new StatusSetStatusPayload(text,emoji,expires_at,false,"manual");
    }

    public StatusSetStatusRequest(String text,String emoji,long expires_at,boolean should_notify,String status_type)
    {
        payload = new StatusSetStatusPayload(text,emoji,expires_at,should_notify,status_type);
    }

}
