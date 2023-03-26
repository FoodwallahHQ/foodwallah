package ai.kalico.api.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Bizuwork Melesse
 * created on 02/18/2023
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "leads")
public class LeadsProps {
    private Long openRateThreshold = 300L;
    private Long minOpenCount = 2L;

}
