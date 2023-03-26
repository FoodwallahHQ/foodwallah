package ai.kalico.api.service.instagram4j.models.discover;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@JsonTypeInfo(defaultImpl = SectionalItem.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "layout_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SectionalMediaGridItem.class)
})
public class SectionalItem extends IGBaseModel {
    private String layout_type;
    private String feed_type;
}
