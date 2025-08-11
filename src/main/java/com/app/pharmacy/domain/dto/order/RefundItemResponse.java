package com.app.pharmacy.domain.dto.order;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefundItemResponse {
    private String medicineName;
    private Integer quantity;
    private BigDecimal refundAmount;
}
