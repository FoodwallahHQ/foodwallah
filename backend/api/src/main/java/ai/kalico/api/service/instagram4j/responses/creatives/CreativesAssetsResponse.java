package ai.kalico.api.service.instagram4j.responses.creatives;

import ai.kalico.api.service.instagram4j.models.creatives.StaticSticker;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreativesAssetsResponse extends IGResponse {
    private List<StaticSticker> static_stickers;
}
