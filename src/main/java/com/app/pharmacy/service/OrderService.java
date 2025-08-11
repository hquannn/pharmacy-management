package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.common.Constants;
import com.app.pharmacy.domain.dto.order.*;
import com.app.pharmacy.domain.entity.*;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.OrderMapper;
import com.app.pharmacy.repository.*;
import com.app.pharmacy.specification.OrderSpecification;
import com.app.pharmacy.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.app.pharmacy.specification.OrderSpecification.hasCustomerId;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderLogRepository orderLogRepository;
    private final CustomerRepository customerRepository;
    private final CustomerPointConfigRepository customerPointConfigRepository;
    private final Clock clock;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse<OrderResponse> createOrder(CreateOrderRequest request, Authentication connectedUser) {
        ApiResponse<OrderResponse> response = new ApiResponse<>();
        LocalDateTime now = LocalDateTime.now(clock);

        Order order = OrderMapper.INSTANCE.toEntity(request, now, connectedUser.getName());
        List<OrderItem> orderItems = OrderMapper.INSTANCE.toListEntity(request.orderItems(), now, connectedUser.getName(), order);
        order.setOrderItems(orderItems);
        final BigDecimal[] totalAmount = {orderItems.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add)};


        customerRepository.findById(request.customerId()).ifPresentOrElse(customer -> {
            if (request.usePoint() != null && request.usePoint()) {
                totalAmount[0] = totalAmount[0].subtract(customer.getPoints());
                customer.setPoints(new BigDecimal(0));
            } else {
                BigDecimal point = customer.getPoints().add(customerPointConfigRepository.findAll().get(0).getRatio().multiply(totalAmount[0]));
                customer.setPoints(point);
            }
            customerRepository.save(customer);
        }, () -> {});
        order.setTotalAmount(totalAmount[0]);
        orderRepository.save(order);

        List<String> cartIds = new ArrayList<>();
        Map<String, Integer> cartRequestMap = new HashMap<>();
        request.orderItems().forEach(orderItemRequest -> {
            cartIds.add(orderItemRequest.cartId());
            cartRequestMap.put(orderItemRequest.cartId(), orderItemRequest.quantity());
        });

        List<Cart> cart = cartRepository
                .findByIdIn(cartIds).stream().peek(
                        i -> {
                            i.setQuantity(i.getQuantity() - cartRequestMap.get(i.getId()));
                            i.setUpdatedBy(connectedUser.getName());
                            i.setUpdatedDate(LocalDateTime.now(clock));
                            cartRequestMap.remove(i.getId());
                        }).toList();
        if (!cartRequestMap.isEmpty()) {
            throw new CustomResponseException(ErrorCode.CART_NOT_EXISTED);
        }
        cartRepository.saveAll(cart);

        String orderCode = Constants.ORDER_CODE_PREFIX
                + StringUtils.generateRandomString(2)
                + StringUtils.generateRandomNumberString(6);
        orderLogRepository.save(OrderLog
                .builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .usePoint(request.usePoint())
                .createdBy(connectedUser.getName())
                .type(OrderType.ORDER)
                .code(orderCode)
                .createdDate(now)
                .build()
        );

        OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
        orderResponse.setType(OrderType.ORDER);
        orderResponse.setCode(orderCode);
        orderResponse.setCreatedBy(connectedUser.getName());
        orderResponse.setCreatedDate(now);
        response.setData(orderResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<OrderResponse>> getOrders(OrderLogRequest request, Pageable pageable, Authentication connectedUser) {
        ApiResponse<CommonGetResponse<OrderResponse>> response = new ApiResponse<>();
        String customerId = connectedUser.getName();
        if (connectedUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))){
            customerId = null;
        }
        Specification<OrderLog> specification = Specification.where(
                OrderSpecification.hasOrderDate(request.orderDateBegin(), request.orderDateEnd())
                        .and(hasCustomerId(customerId))
        );

        Page<OrderLog> orderLogPage = orderLogRepository.findAll(specification, pageable);
        List<OrderResponse> orderResponses = OrderMapper.INSTANCE.toOrderResponseList(orderLogPage.getContent(), cartRepository);
        response.setData(new CommonGetResponse<>(
                orderResponses, orderLogPage.getSize(), orderLogPage.getNumber(), orderLogPage.getTotalElements()
        ));
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse<RefundResponse> refund(List<RefundRequest> request, Authentication connectedUser) {
        ApiResponse<RefundResponse> response = new ApiResponse<>();
        List<RefundItemResponse> refundItemResponses = new ArrayList<>();
        AtomicReference<BigDecimal> totalRefundAmountAtomic = new AtomicReference<>(new BigDecimal(0));
        String orderCode = Constants.REFUND_CODE_PREFIX
                + StringUtils.generateRandomString(2)
                + StringUtils.generateRandomNumberString(6);
        List<String> cartIds = request.stream().map(RefundRequest::refundItemId).distinct().toList();
        Map<String, Integer> requestMap = new HashMap<>();
        request.forEach(r -> requestMap.merge(r.refundItemId(), r.refundItemQuantity(), Integer::sum));
        List<Cart> carts = new ArrayList<>();
        List<String> foundCartIds = new ArrayList<>();
        cartRepository.findByIdIn(cartIds).forEach(cart -> {
            Integer refundQuantity = requestMap.get(cart.getId());
            cart.setQuantity(cart.getQuantity() + refundQuantity);
            BigDecimal refundAmount = cart.getMedicine().getPrice().multiply(BigDecimal.valueOf(refundQuantity));
            totalRefundAmountAtomic.updateAndGet(current -> current.add(refundAmount));
            foundCartIds.add(cart.getId());
            RefundItemResponse refundItemResponse = RefundItemResponse
                    .builder()
                    .medicineName(cart.getMedicine().getName())
                    .refundAmount(refundAmount)
                    .quantity(refundQuantity)
                    .build();
            refundItemResponses.add(refundItemResponse);
        });
        if (foundCartIds.isEmpty()) {
            throw new CustomResponseException(ErrorCode.CART_NOT_EXISTED);
        }
        RefundResponse refundResponse = RefundResponse
                .builder()
                .refundItemResponses(refundItemResponses)
                .type(OrderType.REFUND)
                .code(orderCode)
                .build();
        response.setData(refundResponse);
        cartRepository.saveAll(carts);
        OrderLog saleLog = OrderLog
                .builder()
                .orderId(UUID.randomUUID().toString())
                .createdBy(connectedUser.getName())
                .createdDate(LocalDateTime.now(clock))
                .totalAmount(new BigDecimal(0).subtract(totalRefundAmountAtomic.get()))
                .code(orderCode)
                .type(OrderType.REFUND)
                .refundItemId(String.join(",", foundCartIds))
                .build();
        orderLogRepository.save(saleLog);
        return response;
    }
}
