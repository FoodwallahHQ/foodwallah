package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.media.VoiceMedia;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("voice_media")
public class ThreadVoiceMediaItem extends ThreadItem {
    private VoiceMedia media;
}
