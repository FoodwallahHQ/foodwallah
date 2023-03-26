package ai.kalico.api.service.instagram4j.models.media.thread;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(defaultImpl = ThreadMedia.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ThreadImageMedia.class),
        @JsonSubTypes.Type(value = ThreadVideoMedia.class)
})
public class ThreadMedia extends IGBaseModel {
    private long pk;
    private String id;
    private String media_type;
    private int original_width;
    private int original_height;
}
