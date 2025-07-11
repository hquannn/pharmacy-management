package com.app.pharmacy.domain.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaleChartInfoResponse {
    private BigDecimal amountOfSale;
    private BigDecimal amountOfRefund;
    private BigDecimal totalAmount;

    private Integer quantityOfSale;
    private Integer quantityOfRefund;
    private Integer totalQuantity;;
    private Month month;

}
