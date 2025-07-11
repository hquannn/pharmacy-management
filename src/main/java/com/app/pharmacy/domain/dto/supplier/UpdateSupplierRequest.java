package com.app.pharmacy.domain.dto.supplier;

public record UpdateSupplierRequest(
        String name,
        String address,
        String phoneNo,
        String mail
) {
}
