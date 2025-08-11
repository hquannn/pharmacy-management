package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.cart.CartDTO;
import com.app.pharmacy.domain.dto.cart.GetCartRequest;
import com.app.pharmacy.domain.dto.order.CreateOrderRequest;
import com.app.pharmacy.domain.dto.order.OrderResponse;
import com.app.pharmacy.domain.dto.order.RefundRequest;
import com.app.pharmacy.domain.dto.order.RefundResponse;
import com.app.pharmacy.service.CartService;
import com.app.pharmacy.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;


    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') || hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request, Authentication connectedUser
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request, connectedUser));
    }

    @PostMapping("/refund")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<RefundResponse>> refund(
            @Valid @RequestBody List<RefundRequest> request, Authentication  connectedUser
    ) {
        return ResponseEntity.ok(orderService.refund(request, connectedUser));
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole('CUSTOMER') || hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonGetResponse<CartDTO>>> getCart(
            @ModelAttribute GetCartRequest request,
            @PageableDefault(sort = "expDate", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(cartService.getCart(request, pageable));
    }

}
