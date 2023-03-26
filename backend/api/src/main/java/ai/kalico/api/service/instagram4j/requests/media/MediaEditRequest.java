package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.media.MediaResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class MediaEditRequest extends IGPostRequest<MediaResponse> {
    @NonNull
    private String id, _caption;
    private boolean _igtv_feed_preview;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaEditPayload();
    }

    @Override
    public String path() {
        return "media/" + id + "/edit_media/";
    }

    @Override
    public Class<MediaResponse> getResponseType() {
        return MediaResponse.class;
    }

    @Getter @Setter
    public class MediaEditPayload extends IGPayload {
        private String media_id = id;
        private String caption = _caption;
        private boolean igtv_feed_preview = _igtv_feed_preview;
    }

}
