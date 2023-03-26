package ai.kalico.api.service.instagram4j.responses.music;

import ai.kalico.api.service.instagram4j.models.music.MusicPlaylist;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicBrowseResponse extends IGResponse {
    @JsonDeserialize(converter = BeanToIGMusicPlaylistConverter.class)
    private List<MusicPlaylist> items;

    private static class BeanToIGMusicPlaylistConverter
            extends StdConverter<List<Map<String, Object>>, List<MusicPlaylist>> {
        @Override
        public List<MusicPlaylist> convert(List<Map<String, Object>> value) {
            return value.stream()
                    .filter(m -> m.containsKey("playlist"))
                    .map(m -> IGUtils.convertToView(m.get("playlist"), MusicPlaylist.class))
                    .collect(Collectors.toList());
        }
    }
}
