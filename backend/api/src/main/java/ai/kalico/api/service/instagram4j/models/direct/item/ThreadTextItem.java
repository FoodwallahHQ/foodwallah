package ai.kalico.api.service.instagram4j.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("text")
public class ThreadTextItem extends ThreadItem {
    private String text;
}
