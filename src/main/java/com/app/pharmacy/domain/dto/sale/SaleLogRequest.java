package com.app.pharmacy.domain.dto.sale;

import java.time.LocalDateTime;

public record SaleLogRequest(LocalDateTime saleDateBegin, LocalDateTime saleDateEnd) {
}
