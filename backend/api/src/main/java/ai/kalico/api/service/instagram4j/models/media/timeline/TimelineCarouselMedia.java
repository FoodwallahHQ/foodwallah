package ai.kalico.api.service.instagram4j.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("8")
public class TimelineCarouselMedia extends TimelineMedia {
    private int carousel_media_count;
    private List<CarouselItem> carousel_media;
}
