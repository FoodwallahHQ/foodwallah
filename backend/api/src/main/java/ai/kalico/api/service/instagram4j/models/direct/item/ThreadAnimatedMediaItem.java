package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.media.thread.ThreadAnimatedMedia;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("animated_media")
public class ThreadAnimatedMediaItem extends ThreadItem {
    private ThreadAnimatedMedia media;
}
