package com.app.pharmacy.domain.dto.inventory;

import com.app.pharmacy.domain.dto.locationrack.LocationRackResponse;
import com.app.pharmacy.domain.dto.medicine.MedicineResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDTO {
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
