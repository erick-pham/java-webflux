package com.example.erick.integration.portcontrol.config;

import com.example.erick.integration.portcontrol.PortControlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient; // Sử dụng RestClient thay vì WebClient

@Configuration
@RequiredArgsConstructor
public class PortControlConfig {

    private final PortControlProperties properties;

    @Bean
    public RestClient portControlRestClient(RestClient.Builder builder) {
        String baseUrl = properties.getBaseUrl();
        if (baseUrl == null) {
            throw new IllegalStateException("external.portcontrol.base-url must be configured");
        }

        // Cấu hình RestClient tương tự như WebClient nhưng dùng cho Virtual Threads
        return builder.baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
