package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.order.*;
import com.app.pharmacy.domain.dto.sale.SaleItemRequest;
import com.app.pharmacy.domain.dto.sale.SaleItemResponse;
import com.app.pharmacy.domain.dto.sale.SaleResponse;
import com.app.pharmacy.domain.dto.sale.SaleType;
import com.app.pharmacy.domain.entity.*;
import com.app.pharmacy.repository.CartRepository;
import com.app.pharmacy.repository.InventoryRepository;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Mapper(uses = CartRepository.class)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(CreateOrderRequest request, @Context LocalDateTime now, @Context String createdBy);

    @Mapping(target = "createdDate", expression = "java(now)")
    @Mapping(target = "createdBy", expression = "java(createdBy)")
    @Mapping(target = "id", expression = "java(new com.app.pharmacy.domain.entity.CartOrderId(dto.cartId(), null))")
    @Mapping(target = "order", expression = "java(order)")
    @Mapping(target = "totalPrice", source = "dto", qualifiedByName = "totalPrice")
    OrderItem toEntity(OrderItemRequest dto, @Context LocalDateTime now, @Context String createdBy, @Context Order order);
    List<OrderItem> toListEntity(List<OrderItemRequest> dtos, @Context LocalDateTime now, @Context String createdBy, @Context Order order);

    OrderResponse toOrderResponse(Order order);
    @Mapping(target = "createdBy", source = "customerCreated.firstName")
    @Mapping(target = "medicineName", source = "cart.medicine.name")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems);

    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "orderItems", source = "orderLog", qualifiedByName = "orderItems")
    @Mapping(target = "createdBy", source = "customerCreated.firstName")
    @Mapping(target = "customerId", source = "orderLog", qualifiedByName = "customerId")
    @Mapping(target = "refundMedicineName", expression = "java(mapRefundItemIdsToMedicineName(orderLog, cartRepository))")
    OrderResponse toOrderResponseFromLog(OrderLog orderLog, @Context CartRepository cartRepository);
    List<OrderResponse> toOrderResponseList(List<OrderLog> orderLogs, @Context CartRepository cartRepository);


    @Named("totalPrice")
    default BigDecimal totalPrice(OrderItemRequest dto) {
        return dto.price().multiply(new BigDecimal(dto.quantity()));
    }
    @Named("orderItems")
    default List<OrderItemResponse> orderItems(OrderLog orderLog) {
        if (orderLog.getType().equals(OrderType.REFUND)) {
            return null;
        }
        return toOrderItemResponseList(orderLog.getOrder().getOrderItems());
    }

    @Named("customerId")
    default String toCustomerId(OrderLog orderLog) {
        if (orderLog.getType().equals(OrderType.REFUND)) {
            return null;
        }
        return orderLog.getOrder().getCustomerId();
    }

    default String mapRefundItemIdsToMedicineName(OrderLog orderLog, CartRepository cartRepository) {
        if (!orderLog.getType().equals(OrderType.REFUND)) {
            return null;
        }
        List<Cart> carts = cartRepository.findByIdIn(Arrays.asList(orderLog.getRefundItemId().split(",")));
        return String.join(",", carts.stream().map(cart -> cart.getMedicine().getName()).toList());
    }
}
