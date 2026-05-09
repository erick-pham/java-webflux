package com.example.erick.integration.portcontrol;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external.portcontrol")
@Data
public class PortControlProperties {
    @NotNull(message = "Base URL is required")
    private String baseUrl;

    @NotNull(message = "API Key is required")
    private String apiKey;
}
