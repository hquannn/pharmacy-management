package com.app.pharmacy.domain.dto.supplier;

import jakarta.validation.constraints.NotBlank;

public record CreateSupplierRequest(
        @NotBlank(message = "name is mandatory field")
        String name,
        @NotBlank(message = "address is mandatory field")
        String address,
        @NotBlank(message = "phoneNo no is mandatory field")
        String phoneNo,
        String mail
) {
}
