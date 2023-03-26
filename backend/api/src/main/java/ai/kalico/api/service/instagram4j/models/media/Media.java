package ai.kalico.api.service.instagram4j.models.media;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.media.timeline.Comment.Caption;
import ai.kalico.api.service.instagram4j.models.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Media extends IGBaseModel {
    private long pk;
    private String id;
    private long taken_at;
    private long device_timestamp;
    private String media_type;
    private String code;
    private String client_cache_key;
    private int filter_type;
    private User user;
    private Caption caption;
    private boolean can_viewer_reshare;
    private boolean photo_of_you;
    private boolean can_viewer_save;
}
