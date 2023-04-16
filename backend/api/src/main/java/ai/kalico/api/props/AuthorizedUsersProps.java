package ai.kalico.api.props;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Bizuwork Melesse
 * created on 4/16/23
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "authorized-users")
public class AuthorizedUsersProps {
    private Set<String> emails = new HashSet<>();
}
