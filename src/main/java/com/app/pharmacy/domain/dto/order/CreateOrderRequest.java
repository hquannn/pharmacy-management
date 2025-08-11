package com.app.pharmacy.domain.dto.order;

import com.app.pharmacy.domain.dto.sale.SaleItemRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        String customerId,
        @NotNull(message = "orderItems is mandatory field")
        List<OrderItemRequest> orderItems,
        Boolean usePoint
) {
}
