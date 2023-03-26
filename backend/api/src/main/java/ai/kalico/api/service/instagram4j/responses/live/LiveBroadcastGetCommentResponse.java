package ai.kalico.api.service.instagram4j.responses.live;

import ai.kalico.api.service.instagram4j.models.media.timeline.Comment;
import ai.kalico.api.service.instagram4j.models.media.timeline.Comment.Caption;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastGetCommentResponse extends IGResponse {
    private boolean comment_likes_enabled;
    private List<Comment> comments;
    private int comment_count;
    private Caption caption;
    @JsonProperty("is_first_fetch")
    private boolean is_first_fetch;
}
