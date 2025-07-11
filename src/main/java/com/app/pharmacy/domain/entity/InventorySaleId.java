package com.app.pharmacy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventorySaleId {
    @Column(name = "I_ID", nullable = false)
    private String inventoryId;

    @Column(name = "Sale_ID", nullable = false)
    private String saleId;

}
