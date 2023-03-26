package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.media.MediaInfoResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaInfoRequest extends IGGetRequest<MediaInfoResponse> {
    @NonNull
    private String id;

    @Override
    public String path() {
        return "media/" + id + "/info/";
    }

    @Override
    public Class<MediaInfoResponse> getResponseType() {
        return MediaInfoResponse.class;
    }

}
