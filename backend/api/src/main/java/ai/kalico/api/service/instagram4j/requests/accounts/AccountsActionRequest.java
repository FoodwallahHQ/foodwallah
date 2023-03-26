package ai.kalico.api.service.instagram4j.requests.accounts;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.accounts.AccountsUserResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsActionRequest extends IGPostRequest<AccountsUserResponse> {
    @NonNull
    private AccountsAction action;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return "accounts/" + action.name().toLowerCase() + "/";
    }

    @Override
    public Class<AccountsUserResponse> getResponseType() {
        return AccountsUserResponse.class;
    }

    public static enum AccountsAction {
        SET_PRIVATE, SET_PUBLIC, REMOVE_PROFILE_PICTURE;
    }
}
