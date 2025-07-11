package com.app.pharmacy.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefundResponse {
    private List<RefundItemResponse> refundItemResponses;
    private String code;
    private SaleType type;
}
