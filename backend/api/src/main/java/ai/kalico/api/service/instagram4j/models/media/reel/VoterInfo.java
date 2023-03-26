package ai.kalico.api.service.instagram4j.models.media.reel;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.user.Profile;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VoterInfo extends IGBaseModel {
    private Long poll_id;
    private List<Voter> voters;
    private String max_id;
    private boolean more_available;

    @Getter @Setter
    public static class Voter {
        private Profile user;
        private int vote;
        private Long ts;
    }
}
