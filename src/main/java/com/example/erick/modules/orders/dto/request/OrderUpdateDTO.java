package com.example.erick.modules.orders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateDTO {
    private BigDecimal totalAmount;
    private String status;
    private String description;
}
