package com.app.pharmacy.domain.dto.cart;

import java.time.LocalDate;

public record GetCartRequest(
        LocalDate expireDateBegin, LocalDate expireDateEnd, Integer quantity, String medicineName
) {
}
