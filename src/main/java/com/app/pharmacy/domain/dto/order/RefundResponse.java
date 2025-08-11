package com.app.pharmacy.domain.dto.order;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefundResponse {
    private List<RefundItemResponse> refundItemResponses;
    private String code;
    private OrderType type;
}
