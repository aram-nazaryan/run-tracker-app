package am.run.tracker.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class CorsConfiguration {

    @Value("${undertow.cors.allowed-origin}")
    private String[] allowedOrigin;
    @Value("${undertow.cors.allowed-methods}")
    private String[] allowedMethods;
    @Value("${undertow.cors.allowed-headers}")
    private String[] allowedHeaders;
    @Value("${undertow.cors.exposed-headers}")
    private String[] exposedHeaders;
    @Value("${undertow.cors.allowed-credentials}")
    private Boolean allowedCredentials;
    @Value("${undertow.cors.max-age}")
    private Long maxAge;

    @Bean
    @ConditionalOnProperty(name = "undertow.cors.enabled", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {
        final org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(allowedOrigin));
        corsConfiguration.setAllowedMethods(Arrays.asList(allowedMethods));
        corsConfiguration.setAllowCredentials(allowedCredentials);
        corsConfiguration.setAllowedHeaders(Arrays.asList(allowedHeaders));
        corsConfiguration.setExposedHeaders(Arrays.asList(exposedHeaders));
        corsConfiguration.setMaxAge(maxAge);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.setCorsConfigurations(Map.of("/**", corsConfiguration));

        return source;
    }
}
