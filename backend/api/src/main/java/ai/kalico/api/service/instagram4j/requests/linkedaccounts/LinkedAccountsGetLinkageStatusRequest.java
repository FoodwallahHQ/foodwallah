package ai.kalico.api.service.instagram4j.requests.linkedaccounts;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;

public class LinkedAccountsGetLinkageStatusRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "linked_accounts/get_linkage_status/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
