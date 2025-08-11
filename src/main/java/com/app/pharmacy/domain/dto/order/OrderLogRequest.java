package com.app.pharmacy.domain.dto.order;

import java.time.LocalDateTime;

public record OrderLogRequest(
        LocalDateTime orderDateBegin, LocalDateTime orderDateEnd
) {
}
