package ai.kalico.api.service.instagram4j.requests.challenge;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.IGPayload;
import ai.kalico.api.service.instagram4j.requests.IGPostRequest;
import ai.kalico.api.service.instagram4j.responses.challenge.ChallengeStateResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeSelectVerifyMethodRequest extends IGPostRequest<ChallengeStateResponse> {
    @NonNull
    private final String path;
    @NonNull
    private final String _choice;
    private final boolean resend;

    @Override
    public IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private final String choice = _choice;
        };
    }

    @Override
    public String path() {
        return !resend ? path.substring(1) : path.replace("/challenge/", "challenge/replay/");
    }

    @Override
    public Class<ChallengeStateResponse> getResponseType() {
        return ChallengeStateResponse.class;
    }

}
