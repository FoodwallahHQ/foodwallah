package ai.kalico.api.props;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author Bizuwork Melesse
 * created on 11/05/2022
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "project")
public class ProjectProps {
    private Boolean seedDb;
    private List<String> supportedDomains;
    private Set<String> supportedVideoFormats;
    private Set<String> supportedAudioFormats;
    private long maxJobTime = 60 * 5; // 5 minutes
    private boolean ocrHtmlLineBreaks = false;
    private String baseSiteUrl;
    private String baseImageCdnUrl;
    private Double fps;
    private String urlNotSupportedMessage;
    private String defaultRecipeThumbnail;
    private String siteUrl = "https://foodwallah.com";

}
