package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastLikeResponse extends IGResponse {
    private String likes;
    private String burst_likes;

    @Getter @Setter
    public static class LiveBroadcastGetLikeCountResponse extends LiveBroadcastLikeResponse {
        private long like_ts;
        private List<Liker> likers;

        @Getter @Setter
        public static class Liker {
            private String user_id;
            private String profile_pic_url;
            private int count;
        }
    }
}
