package com.app.pharmacy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MED_UNIT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineUnit {
    @Id
    @Column(name = "MU_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "MU_Unit")
    private String unit;
}
