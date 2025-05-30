package ai.kalico.api.service.instagram4j.models.media.timeline;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(defaultImpl = CarouselItem.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageCarouselItem.class),
        @JsonSubTypes.Type(value = VideoCarouselItem.class)
})
public class CarouselItem extends IGBaseModel {
    private int original_width;
    private int original_height;
    private String media_type;
    private String carousel_parent_id;
}
