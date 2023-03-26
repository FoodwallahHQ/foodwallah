package ai.kalico.api.service.instagram4j.requests.qp;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import ai.kalico.api.service.instagram4j.utils.IGUtils;

public class QpGetCooldowns extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "qp/get_cooldowns/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("signature", IGUtils.generateSignature("{}"));
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
