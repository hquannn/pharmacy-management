package com.app.pharmacy.domain.dto.sale;

import com.app.pharmacy.domain.entity.InventorySaleId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleItemResponse {
    private InventorySaleId id;

    private Integer quantity;
    private BigDecimal totalPrice;
    private String medicineName;

    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
}
