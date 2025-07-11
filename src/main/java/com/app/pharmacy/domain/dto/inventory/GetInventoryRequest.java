package com.app.pharmacy.domain.dto.inventory;

import java.time.LocalDate;

public record GetInventoryRequest(
        LocalDate expireDateBegin, LocalDate expireDateEnd, Integer quantity, String medicineName
) {
}
