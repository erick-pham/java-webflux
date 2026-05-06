package com.example.erick.integration.portcontrol;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Flux;

public interface PortControlClient {
  Flux<JsonNode> createQuote();
}
