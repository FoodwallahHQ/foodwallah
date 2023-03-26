package ai.kalico.api.service.instagram4j.responses.challenge;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChallengeStateResponse extends IGResponse {
    private String step_name;
    private StepData step_data;

    @Getter
    @Setter
    public static class StepData {
        private String choice;
        private String email;
    }
}
