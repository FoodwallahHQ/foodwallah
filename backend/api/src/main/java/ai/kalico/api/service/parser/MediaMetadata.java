package ai.kalico.api.service.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bizuwork Melesse
 * created on 6/21/22
 */
@Getter @Setter
@AllArgsConstructor
public class MediaMetadata {
    private String imageUrl;
    private String gifUrl;
    private String videoUrl;
    private String audioUrl;
    private String mediaType;
    private String encoding;
    private String description;
}
