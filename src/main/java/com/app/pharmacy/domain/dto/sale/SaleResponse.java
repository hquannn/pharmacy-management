package com.app.pharmacy.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {
    private String id;

    private BigDecimal totalAmount;
    private String customerId;
    private List<SaleItemResponse> saleItems;
    private SaleType type;
    private String code;
    private String refundMedicineName;

    private LocalDateTime createdDate;
    private String createdBy;
}
