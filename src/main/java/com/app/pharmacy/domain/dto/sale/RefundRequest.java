package com.app.pharmacy.domain.dto.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefundRequest(
        @NotBlank(message = "refundItemId is mandatory field")
        String refundItemId,
        @NotNull(message = "refundItemQuantity is mandatory field")
        @Min(value = 1, message = "refundItemQuantity should be greater than 0")
        Integer refundItemQuantity

) {
}
