package com.app.pharmacy.domain.dto.sale;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSaleRequest(
        String customerId,
        @NotNull(message = "saleItems is mandatory field")
        List<SaleItemRequest> saleItems,
        Boolean usePoint
) {
}
