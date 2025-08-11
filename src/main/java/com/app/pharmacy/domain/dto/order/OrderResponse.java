package com.app.pharmacy.domain.dto.order;

import com.app.pharmacy.domain.dto.sale.SaleItemResponse;
import com.app.pharmacy.domain.dto.sale.SaleType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String id;

    private BigDecimal totalAmount;
    private String customerId;
    private List<OrderItemResponse> orderItems;
    private OrderType type;
    private String code;
    private String refundMedicineName;

    private LocalDateTime createdDate;
    private String createdBy;
}
