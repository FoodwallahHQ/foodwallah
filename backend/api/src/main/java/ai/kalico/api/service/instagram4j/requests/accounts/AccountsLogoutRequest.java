package ai.kalico.api.service.instagram4j.requests.accounts;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;

public class AccountsLogoutRequest extends IGPostRequest<IGResponse> {

    @Override
    public String path() {
        return "accounts/logout/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

}
