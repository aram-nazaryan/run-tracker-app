package am.run.tracker.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPIDefinition() {
        return new OpenAPI()
                .info(new Info().title("Run Tracker").description("A microservice responsible for User/Run management."));
    }
}
