package ai.kalico.api.service.instagram4j.requests.upload;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaUploadFinishRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String uploadId;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaUploadFinishPayload();
    }

    @Override
    public String path() {
        return "media/upload_finish/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Getter @Setter
    public class MediaUploadFinishPayload extends IGPayload {
        private String upload_id = uploadId;
    }
}
