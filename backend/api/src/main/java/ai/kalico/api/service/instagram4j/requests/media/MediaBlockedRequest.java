package ai.kalico.api.service.instagram4j.requests.media;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;

public class MediaBlockedRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "media/blocked/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
