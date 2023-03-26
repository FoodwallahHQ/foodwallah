package ai.kalico.api.service.instagram4j.models.media;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.user.Profile;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserTags extends IGBaseModel {
    private List<UserTag> in;

    @Getter @Setter
    public static class UserTag {
        private Profile user;
        private double[] position;
        private long start_time_in_video_sec;
        private long duration_in_video_in_sec;
    }

    @Getter
    @Setter
    public static class UserTagPayload {
        private final long user_id;
        private final double[] position;

        public UserTagPayload(long pk, double x, double y) {
            this.user_id = pk;
            position = new double[] {x, y};
        }
    }
}
