package ai.kalico.api.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Bizuwork Melesse
 * created on 2/12/23
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "whisper")
public class WhisperProps {
    private String model = "tiny";
}
