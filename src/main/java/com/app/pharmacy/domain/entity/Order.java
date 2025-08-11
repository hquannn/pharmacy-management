package com.app.pharmacy.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @Column(name = "Order_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name =  "Total_Amt")
    private BigDecimal totalAmount;
    @Column(name = "C_ID")
    private String customerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}
