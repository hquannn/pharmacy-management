package com.app.pharmacy.domain.dto.medicine;


import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineScanResponse {
    private String name;
    private BigDecimal price;
    private String categoryId;
    private String medicineUnitId;
}
