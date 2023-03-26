package ai.kalico.api;

import ai.kalico.api.controller.ControllerConfiguration;
import ai.kalico.api.data.DataConfiguration;
import ai.kalico.api.props.PropConfiguration;
import ai.kalico.api.service.ServiceConfiguration;
import ai.kalico.api.utils.migration.FlywayMigration;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bizuwork Melesse
 * created on 2/13/21
 */
@Slf4j
@Configuration
@Import({
        ControllerConfiguration.class,
        DataConfiguration.class,
        PropConfiguration.class,
        ServiceConfiguration.class
})
@RequiredArgsConstructor
public class RootConfiguration {
  private final FlywayMigration flywayMigration;
  public static final ExecutorService executor = Executors.newCachedThreadPool();

  @PostConstruct
  public void onStart() {
    flywayMigration.migrate(false);
  }
}
