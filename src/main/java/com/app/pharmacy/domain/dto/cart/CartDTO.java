package com.app.pharmacy.domain.dto.cart;

import com.app.pharmacy.domain.dto.locationrack.LocationRackResponse;
import com.app.pharmacy.domain.dto.medicine.MedicineResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private String id;

    private MedicineResponse medicine;
    private LocationRackResponse locationRack;
    private Integer quantity;
    private LocalDate mfgDate;
    private LocalDate expDate;
    private Boolean isGettingExpire;

    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
}
