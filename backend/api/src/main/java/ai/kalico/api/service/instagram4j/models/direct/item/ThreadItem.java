package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(defaultImpl = ThreadItem.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "item_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ThreadTextItem.class),
        @JsonSubTypes.Type(value = ThreadMediaShareItem.class),
        @JsonSubTypes.Type(value = ThreadStoryShareItem.class),
        @JsonSubTypes.Type(value = ThreadLinkItem.class),
        @JsonSubTypes.Type(value = ThreadVoiceMediaItem.class),
        @JsonSubTypes.Type(value = ThreadMediaItem.class),
        @JsonSubTypes.Type(value = ThreadProfileItem.class),
        @JsonSubTypes.Type(value = ThreadRavenMediaItem.class),
        @JsonSubTypes.Type(value = ThreadClipItem.class)
})
public class ThreadItem extends IGBaseModel {
    private String item_id;
    private long user_id;
    private long timestamp;
    private String item_type;
    private String client_context;
    private boolean show_forward_attribution;
}
