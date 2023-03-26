package ai.kalico.api.service.instagram4j.requests.music;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.music.MusicTrackResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class MusicSearchRequest extends IGPostRequest<MusicTrackResponse> {
    @NonNull
    private String _query;
    private String _cursor = "0";

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MusicQueryPayload(client.getSessionId(), _cursor, _query);
    }

    @Override
    public String path() {
        return "music/search/";
    }

    @Override
    public Class<MusicTrackResponse> getResponseType() {
        return MusicTrackResponse.class;
    }

    @Getter @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_NULL)
    protected static class MusicQueryPayload extends IGPayload {
        @NonNull
        private String browse_session_id;
        @NonNull
        private String cursor;
        private String q;
    }

}
