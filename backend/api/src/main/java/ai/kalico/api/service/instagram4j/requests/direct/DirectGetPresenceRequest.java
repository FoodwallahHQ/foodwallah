package ai.kalico.api.service.instagram4j.requests.direct;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.direct.DirectGetPresenceResponse;

public class DirectGetPresenceRequest extends IGGetRequest<DirectGetPresenceResponse> {

    @Override
    public String path() {
        return "direct_v2/get_presence/";
    }

    @Override
    public Class<DirectGetPresenceResponse> getResponseType() {
        return DirectGetPresenceResponse.class;
    }

}
