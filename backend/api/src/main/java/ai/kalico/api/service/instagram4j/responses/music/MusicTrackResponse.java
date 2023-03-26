package ai.kalico.api.service.instagram4j.responses.music;

import ai.kalico.api.service.instagram4j.models.music.MusicPlaylist.BeanToTrackConverter;
import ai.kalico.api.service.instagram4j.models.music.MusicTrack;
import ai.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicTrackResponse extends IGResponse implements IGPaginatedResponse {
    @JsonDeserialize(converter = BeanToTrackConverter.class)
    private List<MusicTrack> items;
    private MusicTrackPageInfo page_info;

    public String getNext_max_id() {
        return page_info.getNext_max_id();
    }

    public boolean isMore_available() {
        return page_info.isMore_available();
    }

    @Getter @Setter
    public static class MusicTrackPageInfo {
        private String next_max_id;
        private boolean more_available;
    }
}
