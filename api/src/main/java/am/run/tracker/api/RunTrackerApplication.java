package am.run.tracker.api;


import am.run.tracker.api.config.CorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@ComponentScan(basePackages = {
        "am.run.tracker.core",
        "am.run.tracker.core.persistence.repositories",
        "am.run.tracker.api.rest"
})
@Import({CorsConfiguration.class})
public class RunTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunTrackerApplication.class, args);
    }
}
