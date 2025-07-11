package com.app.pharmacy.domain.dto.medicine;

import com.app.pharmacy.domain.dto.medicinecategory.MedicineCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private MedicineCategoryResponse category;
    private MedUnitResponse unit;

    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
}
