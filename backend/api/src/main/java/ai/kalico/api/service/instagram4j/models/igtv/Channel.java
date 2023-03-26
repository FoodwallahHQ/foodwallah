package ai.kalico.api.service.instagram4j.models.igtv;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineVideoMedia;
import ai.kalico.api.service.instagram4j.models.user.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Channel extends IGBaseModel {
    private String id;
    private List<TimelineVideoMedia> items;
    private boolean more_available;
    private String title;
    private String type;
    private String max_id;
    private User user_dict;
    private String description;
    private String cover_photo_url;
}
