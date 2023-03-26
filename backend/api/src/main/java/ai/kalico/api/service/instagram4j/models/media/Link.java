package ai.kalico.api.service.instagram4j.models.media;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Link {
    private String text;
    private LinkContext link_context;
    private String client_context;
    private String mutation_token;

    @Getter @Setter
    public static class LinkContext {
        private String link_url;
        private String link_title;
        private String link_summary;
        private String link_image_url;
    }
}
