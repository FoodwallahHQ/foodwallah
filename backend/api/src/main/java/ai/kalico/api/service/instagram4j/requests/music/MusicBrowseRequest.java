package ai.kalico.api.service.instagram4j.requests.music;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.music.MusicBrowseResponse;
import lombok.Getter;

public class MusicBrowseRequest extends IGPostRequest<MusicBrowseResponse> {

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String session_id = client.getSessionId();
        };
    }

    @Override
    public String path() {
        return "music/browse/";
    }

    @Override
    public Class<MusicBrowseResponse> getResponseType() {
        return MusicBrowseResponse.class;
    }

}
