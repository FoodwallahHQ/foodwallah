package ai.kalico.api.service.instagram4j.requests.music;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.requests.music.MusicSearchRequest.MusicQueryPayload;
import ai.kalico.api.service.instagram4j.responses.music.MusicTrackResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class MusicGenresIdRequest extends IGPostRequest<MusicTrackResponse> {
    @NonNull
    private String _id;
    private String _cursor = "0";

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MusicQueryPayload(client.getSessionId(), _cursor);
    }

    @Override
    public String path() {
        return "music/genres/" + _id + "/";
    }

    @Override
    public Class<MusicTrackResponse> getResponseType() {
        return MusicTrackResponse.class;
    }

}
