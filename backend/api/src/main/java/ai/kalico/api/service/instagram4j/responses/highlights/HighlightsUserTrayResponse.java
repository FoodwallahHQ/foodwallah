package ai.kalico.api.service.instagram4j.responses.highlights;

import ai.kalico.api.service.instagram4j.models.highlights.Highlight;
import ai.kalico.api.service.instagram4j.models.igtv.Channel;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HighlightsUserTrayResponse extends IGResponse {
    private List<Highlight> tray;
    private Channel tv_channel;
}
