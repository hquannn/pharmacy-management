package com.app.pharmacy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartOrderId {
    @Column(name = "Cart_ID", nullable = false)
    private String cartId;

    @Column(name = "Order_ID", nullable = false)
    private String orderId;
}
