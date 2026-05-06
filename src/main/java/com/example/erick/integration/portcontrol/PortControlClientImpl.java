package com.example.erick.integration.portcontrol;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatusCode;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.erick.integration.portcontrol.dto.CreateQuoteRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PortControlClientImpl implements PortControlClient {

  private final WebClient portControlWebClient;

  @Override
  public Flux<JsonNode> createQuote() {

    CreateQuoteRequest request = CreateQuoteRequest.builder()
        .quotation(
            CreateQuoteRequest.Quotation.builder()
                .product(
                    CreateQuoteRequest.Product.builder()
                        .code("ENT")
                        .build())
                .insuredPersons(List.of(
                    CreateQuoteRequest.InsuredPerson.builder()
                        .role("SELF")
                        .personalDetails(
                            CreateQuoteRequest.PersonalDetails.builder()
                                .dob("04/12/1997")
                                .gender("M")
                                .build())
                        .build()))
                .build())
        .userMetadata(
            CreateQuoteRequest.UserMetadata.builder()
                .tenantId("57c6c934-d40d-45e4-862c-d411ddb99927")
                .channelId("19928da5-3d4f-4d4f-aeac-e1d2dbdc9c17")
                .channelName("360F.EPOS.CHANNEL-DIRECT")
                .role("EPOSAppRole.SalesRep")
                .userId("1c37280d-28d6-48d2-9cf8-3ba22dc07925")
                .userPublicId("PAUL-DIR-0000008")
                .build())
        .build();

    return portControlWebClient
        .post()
        .uri("/api/v1/orchestration/360-quote/next-gen/execute")
        .header("X-TENANT-ID", "57c6c934-d40d-45e4-862c-d411ddb99927")
        .header("X-CHANNEL-ID", "19928da5-3d4f-4d4f-aeac-e1d2dbdc9c17")
        .bodyValue(Objects.requireNonNull(request))
        .retrieve().onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class)
            .map(body -> new RuntimeException("PortControl error: " + body)))
        .bodyToFlux(JsonNode.class);
  }
}