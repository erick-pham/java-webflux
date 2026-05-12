package com.example.erick.modules.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDashboardDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private List<OrderDashboardDTO> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderDashboardDTO {
        private Long id;
        private BigDecimal totalAmount;
        private String status;
        private String description;
        private PaymentDashboardDTO payment;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentDashboardDTO {
        private Long id;
        private BigDecimal amount;
        private String paymentMethod;
        private String status;
    }
}
