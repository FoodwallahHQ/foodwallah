package ai.kalico.api.service.instagram4j.models.music;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicLyrics {
    private List<LyricPhrase> phrases;

    @Getter @Setter
    public static class LyricPhrase extends IGBaseModel {
        private long start_time_in_ms;
        private long end_time_in_ms;
        private String phrase;
    }
}
