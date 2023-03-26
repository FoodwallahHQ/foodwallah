package ai.kalico.api.service.instagram4j.requests.loom;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;

public class LoomFetchConfigRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "loom/fetch_config/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
