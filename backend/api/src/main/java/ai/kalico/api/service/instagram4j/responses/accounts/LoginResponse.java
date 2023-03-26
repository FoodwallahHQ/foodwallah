package ai.kalico.api.service.instagram4j.responses.accounts;

import ai.kalico.api.service.instagram4j.models.user.User;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import ai.kalico.api.service.instagram4j.responses.challenge.Challenge;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginResponse extends IGResponse {
    private User logged_in_user;
    private Challenge challenge;
    private TwoFactorInfo two_factor_info;

    @Getter @Setter
    public class TwoFactorInfo {
        private String two_factor_identifier;
    }
}
