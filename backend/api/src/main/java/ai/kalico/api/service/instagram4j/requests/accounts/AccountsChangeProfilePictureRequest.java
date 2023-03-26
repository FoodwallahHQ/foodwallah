package ai.kalico.api.service.instagram4j.requests.accounts;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.accounts.AccountsUserResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsChangeProfilePictureRequest extends IGPostRequest<AccountsUserResponse> {
    @NonNull
    private String _upload_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String upload_id = _upload_id;
        };
    }

    @Override
    public String path() {
        return "accounts/change_profile_picture/";
    }

    @Override
    public Class<AccountsUserResponse> getResponseType() {
        return AccountsUserResponse.class;
    }

}
