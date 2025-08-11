package com.app.pharmacy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CART")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @Column(name = "Cart_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name =  "Med_ID", referencedColumnName = "Med_ID", updatable = false)
    private Medicine medicine;
    @Column(name = "LR_ID")
    private String locationRackId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LR_ID", referencedColumnName = "LR_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private LocationRack locationRack;
    @Column(name = "I_Qty")
    private Integer quantity;
    @Column(name = "Mfg_Date")
    private LocalDate mfgDate;
    @Column(name = "Exp_Date")
    private LocalDate expDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "E_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Employee employeeCreated;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "updated_by")
    private String updatedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "C_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Customer customerUpdated;
}
