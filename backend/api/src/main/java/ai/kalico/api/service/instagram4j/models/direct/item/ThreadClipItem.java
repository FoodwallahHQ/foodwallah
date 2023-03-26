package ai.kalico.api.service.instagram4j.models.direct.item;

import ai.kalico.api.service.instagram4j.models.media.Media;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("clip")
public class ThreadClipItem extends ThreadItem{
    @JsonProperty("clip")
    @JsonDeserialize(converter = ClipToMedia.class)
    private Media media;

    private static class ClipToMedia
            extends StdConverter<Map<String, Object>, Media> {
        @Override
        public Media convert(Map<String, Object> value) {
            return IGUtils.convertToView(value.get("clip"), Media.class);
        }
    }
}
