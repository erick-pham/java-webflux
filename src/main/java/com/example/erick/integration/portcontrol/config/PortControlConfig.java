package com.example.erick.integration.portcontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;

import com.example.erick.integration.portcontrol.PortControlProperties;

@Configuration
@RequiredArgsConstructor
public class PortControlConfig {

  private final PortControlProperties properties;

  @Bean
  public WebClient portControlWebClient() {
    String baseUrl = properties.getBaseUrl();
    if (baseUrl == null) {
      throw new IllegalStateException("external.portcontrol.base-url must be configured");
    }

    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
