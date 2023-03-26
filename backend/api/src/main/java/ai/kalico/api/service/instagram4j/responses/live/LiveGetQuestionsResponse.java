package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.models.user.Profile;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveGetQuestionsResponse extends IGResponse {
    private List<LiveQuestions> questions;

    @Getter @Setter
    public static class LiveQuestions {
        private String text;
        private long qid;
        private String source;
        private Profile user;
        private long timestamp;
    }
}
