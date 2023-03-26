package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.media.Link;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("link")
public class ThreadLinkItem extends ThreadItem {
    private Link link;
}
