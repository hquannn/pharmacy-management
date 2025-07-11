package com.app.pharmacy.domain.dto.purchase;

import java.time.LocalDateTime;

public record GetPurchaseRequest( String medicineName, String supplierName, LocalDateTime purDateBegin, LocalDateTime purDateEnd) {
}
