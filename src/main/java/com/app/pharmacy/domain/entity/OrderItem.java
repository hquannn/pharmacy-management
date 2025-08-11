package com.app.pharmacy.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_ITEM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @EmbeddedId
    private CartOrderId id;

    @Column(name = "Order_Qty")
    private Integer quantity;
    @Column(name = "Tot_Price")
    private BigDecimal totalPrice;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "Order_ID", referencedColumnName = "Order_ID")
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Cart_ID", referencedColumnName = "Cart_ID",  foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Cart cart;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "created_by")
    private String createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "C_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Customer customerCreated;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "updated_by")
    private String updatedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "C_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Customer customerUpdated;

}
