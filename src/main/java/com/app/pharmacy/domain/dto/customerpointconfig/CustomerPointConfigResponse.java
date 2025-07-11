package com.app.pharmacy.domain.dto.customerpointconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPointConfigResponse {
    private String id;

    private BigDecimal ratio;

    private LocalDateTime updatedDate;
    private String updatedBy;
}
