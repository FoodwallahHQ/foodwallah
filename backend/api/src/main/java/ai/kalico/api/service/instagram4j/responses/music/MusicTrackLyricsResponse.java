package ai.kalico.api.service.instagram4j.responses.music;

import ai.kalico.api.service.instagram4j.models.music.MusicLyrics;
import ai.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicTrackLyricsResponse extends IGResponse {
    private MusicLyrics lyrics;
}
