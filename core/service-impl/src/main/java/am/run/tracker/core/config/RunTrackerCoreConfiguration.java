package am.run.tracker.core.config;

import am.run.tracker.core.persistence.repositories.config.RunTrackerPersistenceConfiguration;
import am.run.tracker.core.persistence.repositories.utils.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({RunTrackerPersistenceConfiguration.class})
@PropertySource(value = "classpath:/am/run/tracker/run-tracker-service.yml", factory = YamlPropertySourceFactory.class)
public class RunTrackerCoreConfiguration {

}
