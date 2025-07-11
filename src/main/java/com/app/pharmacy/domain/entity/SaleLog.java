package com.app.pharmacy.domain.entity;

import com.app.pharmacy.domain.dto.sale.SaleType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SALE_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleLog {
    @Id
    @Column(name = "Sale_ID")
    private String saleId;

    @Column(name = "use_point")
    private Boolean usePoint;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "Total_Amt")
    private BigDecimal totalAmount;
    @Column(name = "Sale_Code")
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SaleType type;
    @Column(name = "refund_item_id")
    private String refundItemId;

    @Column(name = "created_by")
    private String createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "E_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Employee employeeCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Sale_ID", referencedColumnName = "Sale_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), updatable = false, insertable = false)
    private Sale sale;
}
