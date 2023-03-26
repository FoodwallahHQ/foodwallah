package ai.kalico.api.service.instagram4j.models.media.timeline;

import ai.kalico.api.service.instagram4j.models.location.Location;
import ai.kalico.api.service.instagram4j.models.media.Media;
import ai.kalico.api.service.instagram4j.models.media.UserTags;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(defaultImpl = Media.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TimelineImageMedia.class),
        @JsonSubTypes.Type(value = TimelineVideoMedia.class),
        @JsonSubTypes.Type(value = TimelineCarouselMedia.class)
})
public class TimelineMedia extends Media {
    private List<Comment> preview_comments;
    private boolean has_liked;
    private int like_count;
    private int comment_count;
    private Location location;
    private UserTags usertags;
}
