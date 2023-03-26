package ai.kalico.api.service.instagram4j.models.media.reel;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.user.Profile;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponderInfo extends IGBaseModel {
    private Long question_id;
    private String question;
    private String question_type;
    private List<Responder> responders;
    private String max_id;
    private boolean more_available;

    @Getter @Setter
    public static class Responder {
        private Profile user;
        private String response;
        private String id;
        private Long ts;
    }
}
