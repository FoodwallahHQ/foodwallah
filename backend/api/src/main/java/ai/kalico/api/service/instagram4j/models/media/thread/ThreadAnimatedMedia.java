package ai.kalico.api.service.instagram4j.models.media.thread;

import ai.kalico.api.service.instagram4j.models.IGBaseModel;
import ai.kalico.api.service.instagram4j.models.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ThreadAnimatedMedia extends IGBaseModel {
    private ThreadImages images;
    @JsonProperty("is_random")
    private boolean is_random;
    @JsonProperty("is_sticker")
    private boolean is_sticker;
    private User user;

    @Getter @Setter
    public static class ThreadImages {
        private FixedHeight fixed_height;
    }

    @Getter @Setter
    public static class FixedHeight {
        private String height;
        private String mp4;
        private String mp4_size;
        private String size;
        private String url;
        private String webp;
        private String webp_size;
        private String width;
    }
}
