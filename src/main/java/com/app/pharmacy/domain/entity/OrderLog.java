package com.app.pharmacy.domain.entity;

import com.app.pharmacy.domain.dto.order.OrderType;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLog {
    @Id
    @Column(name = "Order_ID")
    private String orderId;

    @Column(name = "use_point")
    private Boolean usePoint;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "Total_Amt")
    private BigDecimal totalAmount;
    @Column(name = "Order_Code")
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;
    @Column(name = "refund_item_id")
    private String refundItemId;

    @Column(name = "created_by")
    private String createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "C_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Customer customerCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID", referencedColumnName = "Order_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), updatable = false, insertable = false)
    private Order order;
}
