package ai.kalico.api.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Bizuwork Melesse
 * created on 01/02/2023
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "docker-images")
public class DockerImageProps {
    private String tesseract = "tesseractshadow/tesseract4re:latest";
    private String imageMagick = "dpokidov/imagemagick:latest";
    private String ffmpeg = "jrottenberg/ffmpeg:latest";
    private String whisper = "537408061242.dkr.ecr.us-east-2.amazonaws.com/whisperai:latest";
}
