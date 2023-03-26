package ai.kalico.api.service.instagram4j.requests.accounts;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.accounts.AccountsUserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
public class AccountsEditProfileRequest extends IGPostRequest<AccountsUserResponse> {
    @NonNull
    private AccountsEditProfilePayload payload;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return payload;
    }

    @Override
    public String path() {
        return "accounts/edit_profile/";
    }

    @Override
    public Class<AccountsUserResponse> getResponseType() {
        return AccountsUserResponse.class;
    }

    @Data
    @Setter
    @Accessors(fluent = true)
    @EqualsAndHashCode(callSuper=false)
    public static class AccountsEditProfilePayload extends IGPayload {
        @JsonProperty("first_name")
        private String first_name;
        @JsonProperty("biography")
        private String biography;
        @NonNull
        @JsonProperty("username")
        private final String username;
        @JsonProperty("phone_number")
        private String phone_number;
        @NonNull
        @JsonProperty("email")
        private final String email;
        @JsonProperty("external_url")
        private String external_url;
    }

}
