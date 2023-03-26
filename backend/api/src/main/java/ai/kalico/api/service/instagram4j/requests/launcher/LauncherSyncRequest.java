package ai.kalico.api.service.instagram4j.requests.launcher;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.IGConstants;
import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class LauncherSyncRequest extends IGPostRequest<IGResponse> {
    private boolean preLogin;

    @Override
    public String baseApiUrl() {
        return preLogin ? IGConstants.B_BASE_API_URL : super.baseApiUrl();
    }

    @Override
    protected IGBaseModel getPayload(IGClient client) {
        return preLogin ? new PrePayload(client.getPhoneId()) : new PostPayload();
    }

    @Override
    public String path() {
        return "launcher/sync/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    @Setter
    @EqualsAndHashCode(callSuper=false)
    private class PrePayload extends IGBaseModel {
        private final String id;
        private final String server_config_retrieval = "1";
    }

    @Data
    @Setter
    @EqualsAndHashCode(callSuper=false)
    private class PostPayload extends IGPayload {
        private final String server_config_retrieval = "1";
    }

}
