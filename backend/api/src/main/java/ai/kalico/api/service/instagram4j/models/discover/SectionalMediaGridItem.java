package ai.kalico.api.service.instagram4j.models.discover;

import ai.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("media_grid")
public class SectionalMediaGridItem extends SectionalItem {
    @JsonProperty("layout_content")
    @JsonDeserialize(converter = LayoutContentToIGTimelineMedia.class)
    private List<TimelineMedia> medias;

    private static class LayoutContentToIGTimelineMedia
            extends StdConverter<Map<String, List<Map<String, Object>>>, List<TimelineMedia>> {
        @Override
        public List<TimelineMedia> convert(Map<String, List<Map<String, Object>>> value) {
            return value.get("medias").stream()
                    .map(m -> IGUtils.convertToView(m.get("media"), TimelineMedia.class))
                    .collect(Collectors.toList());
        }
    }
}
