package com.example.erick.integration.portcontrol;

import com.example.erick.integration.portcontrol.dto.CreateQuoteRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient; // Đổi sang RestClient

@Service
@RequiredArgsConstructor
@Slf4j
public class PortControlClientImpl implements PortControlClient {
    // Inject RestClient đã cấu hình ở bước trước
    private final RestClient portControlRestClient;

    @Override
    public JsonNode createQuote() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 1. Lấy RequestID từ MDC (đã được set bởi Filter)
        String traceId = MDC.get("traceId");
        log.info("PortControlClientImpl Bắt đầu xử lý với ID: {}", traceId);
        CreateQuoteRequest request = CreateQuoteRequest.builder()
                .quotation(
                        CreateQuoteRequest.Quotation.builder()
                                .product(
                                        CreateQuoteRequest.Product.builder()
                                                .code("ENT")
                                                .build())
                                .insuredPersons(
                                        List.of(
                                                CreateQuoteRequest.InsuredPerson.builder()
                                                        .role("SELF")
                                                        .personalDetails(
                                                                CreateQuoteRequest.PersonalDetails
                                                                        .builder()
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

        // 2. Thực hiện gọi API đồng bộ
        Object rawResponse = portControlRestClient
                .post()
                .uri("/api/v1/orchestration/360-quote/next-gen/execute")
                .header("X-TENANT-ID", "57c6c934-d40d-45e4-862c-d411ddb99927")
                .header("X-CHANNEL-ID", "19928da5-3d4f-4d4f-aeac-e1d2dbdc9c17")
                // Đính kèm RequestID để trace log xuyên suốt các service
                .header("X-Request-ID", traceId)
                .body(Objects.requireNonNull(request))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            // Xử lý lỗi đồng bộ, không cần map hay Mono nữa
                            byte[] body = res.getBody().readAllBytes();
                            throw new RuntimeException(
                                    "PortControl error: " + new String(body));
                        })
                // Chuyển kết quả trực tiếp sang List (thay vì Flux)
                .body(Object.class);
        System.out.println(
                "PC-Quote Creation Response: " + objectMapper.valueToTree(rawResponse).toString());
        return objectMapper.valueToTree(rawResponse);
    }
}
