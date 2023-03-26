package ai.kalico.api.service.instagram4j.models.creatives;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StaticSticker {
    private String id;
    private List<Sticker> stickers;
    private List<String> keywords;

    @Getter @Setter
    public static class Sticker {
        private String id;
        private String type;
        private String name;
        private String image_url;
        private double image_width_ratio;
        private double tray_image_width_ratio;
        private double image_width;
        private double image_height;
    }
}
