package ai.kalico.api.service.instagram4j.requests.challenge;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.accounts.LoginResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeSendCodeRequest extends IGPostRequest<LoginResponse> {
    @NonNull
    private String path;
    @NonNull
    private String code;

    @Override
    public IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private final String security_code = code;
        };
    }

    @Override
    public String path() {
        return path.substring(1);
    }

    @Override
    public Class<LoginResponse> getResponseType() {
        return LoginResponse.class;
    }

}
