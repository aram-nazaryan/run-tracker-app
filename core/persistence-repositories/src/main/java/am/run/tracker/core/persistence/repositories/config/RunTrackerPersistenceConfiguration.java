package am.run.tracker.core.persistence.repositories.config;

import am.run.tracker.core.persistence.repositories.utils.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"am.run.tracker.core.persistence.repositories"})
@EnableJpaAuditing
@EntityScan(basePackages = {"am.run.tracker.core.persistence.entities"})
@PropertySource(value = "classpath:/am/run/tracker/run-tracker-persistence.yml", factory = YamlPropertySourceFactory.class)
public class RunTrackerPersistenceConfiguration {

}
