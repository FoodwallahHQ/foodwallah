package ai.kalico.api.service.instagram4j.responses.music;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicGetResponse extends IGResponse {
    @JsonProperty("items")
    @JsonDeserialize(converter = BeanToIdConverter.class)
    private List<String> ids;

    private static class BeanToIdConverter
            extends StdConverter<List<Map<String, Map<String, String>>>, List<String>> {
        @Override
        public List<String> convert(List<Map<String, Map<String, String>>> value) {
            return value.stream()
                    .flatMap(m -> m.values().stream().map(b -> b.get("id"))
                            .collect(Collectors.toList()).stream())
                    .collect(Collectors.toList());
        }
    }
}
