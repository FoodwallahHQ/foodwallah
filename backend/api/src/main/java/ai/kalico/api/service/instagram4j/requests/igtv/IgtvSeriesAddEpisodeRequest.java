package ai.kalico.api.service.instagram4j.requests.igtv;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.igtv.IgtvSeriesResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IgtvSeriesAddEpisodeRequest extends IGPostRequest<IgtvSeriesResponse> {
    @NonNull
    private String _series;
    @NonNull
    private Long pk;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvSeriesAddEpisodePayload();
    }

    @Override
    public String path() {
        return "igtv/series/" + _series + "/add_episode/";
    }

    @Override
    public Class<IgtvSeriesResponse> getResponseType() {
        return IgtvSeriesResponse.class;
    }

    @Getter @Setter
    public class IgtvSeriesAddEpisodePayload extends IGPayload {
        private String media_id = pk.toString();
    }

}
