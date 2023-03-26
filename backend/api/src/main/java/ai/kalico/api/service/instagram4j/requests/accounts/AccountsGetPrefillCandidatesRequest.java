package ai.kalico.api.service.instagram4j.requests.accounts;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.IGConstants;
import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

public class AccountsGetPrefillCandidatesRequest extends IGPostRequest<IGResponse> {

    @Override
    public String baseApiUrl() {
        return IGConstants.B_BASE_API_URL;
    }

    @Override
    protected IGBaseModel getPayload(IGClient client) {
        return new PrePayload(client.getDeviceId(), client.getPhoneId(), client.getGuid());
    }

    @Override
    public String path() {
        return "accounts/get_prefill_candidates/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    @Setter
    @EqualsAndHashCode(callSuper=false)
    private class PrePayload extends IGBaseModel {
        private final String android_device_id;
        private final String phone_id;
        private final String device_id;
        private final String usages = "[\"account_recovery_omnibox\"]";
    }

}
