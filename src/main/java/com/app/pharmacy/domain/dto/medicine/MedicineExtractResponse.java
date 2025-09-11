package com.app.pharmacy.domain.dto.medicine;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineExtractResponse {
    private String name;
    private BigDecimal price;
    private String category;
    private String unit;
    private String ingredients;
    private String description;
}
