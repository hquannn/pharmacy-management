package com.app.pharmacy.domain.dto.locationrack;

import jakarta.validation.constraints.NotBlank;

public record CreateLocationRackRequest(
        @NotBlank(message = "position is mandatory field")
        String position
) {
}
