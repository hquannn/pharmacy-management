package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.sale.CreateSaleRequest;
import com.app.pharmacy.domain.dto.sale.SaleItemRequest;
import com.app.pharmacy.domain.dto.sale.SaleItemResponse;
import com.app.pharmacy.domain.dto.sale.SaleResponse;
import com.app.pharmacy.domain.dto.sale.SaleType;
import com.app.pharmacy.domain.entity.Inventory;
import com.app.pharmacy.domain.entity.Sale;
import com.app.pharmacy.domain.entity.SaleItem;
import com.app.pharmacy.domain.entity.SaleLog;
import com.app.pharmacy.repository.InventoryRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = InventoryRepository.class)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleMapper {
    SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "saleItems", ignore = true)
    Sale toEntity(CreateSaleRequest request, @Context LocalDateTime now, @Context String createdBy);

    @Mapping(target = "createdDate", expression = "java(now)")
    @Mapping(target = "createdBy", expression = "java(createdBy)")
    @Mapping(target = "id", expression = "java(new com.app.pharmacy.domain.entity.InventorySaleId(dto.inventoryId(), null))")
    @Mapping(target = "sale", expression = "java(sale)")
    @Mapping(target = "totalPrice", source = "dto", qualifiedByName = "totalPrice")
    SaleItem toEntity(SaleItemRequest dto, @Context LocalDateTime now, @Context String createdBy, @Context Sale sale);
    List<SaleItem> toListEntity(List<SaleItemRequest> dtos, @Context LocalDateTime now, @Context String createdBy, @Context Sale sale);

    SaleResponse toSaleResponse(Sale sale);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "medicineName", source = "inventory.medicine.name")
    SaleItemResponse toSaleItemResponse(SaleItem saleItem);

    List<SaleItemResponse> toSaleItemResponseList(List<SaleItem> saleItems);

    @Mapping(target = "id", source = "saleId")
    @Mapping(target = "saleItems", source = "saleLog", qualifiedByName = "saleItems")
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "customerId", source = "saleLog", qualifiedByName = "customerId")
    @Mapping(target = "refundMedicineName", expression = "java(mapRefundItemIdsToMedicineName(saleLog, inventoryRepository))")
    SaleResponse toSaleResponseFromLog(SaleLog saleLog, @Context InventoryRepository inventoryRepository);
    List<SaleResponse> toSaleResponseList(List<SaleLog> saleLogs, @Context InventoryRepository inventoryRepository);

    @Named("totalPrice")
    default BigDecimal totalPrice(SaleItemRequest dto) {
        return dto.price().multiply(new BigDecimal(dto.quantity()));
    }
    @Named("saleItems")
    default List<SaleItemResponse> saleItems(SaleLog saleLog) {
        if (saleLog.getType().equals(SaleType.REFUND)) {
            return null;
        }
        return toSaleItemResponseList(saleLog.getSale().getSaleItems());
    }

    @Named("customerId")
    default String toCustomerId(SaleLog saleLog) {
        if (saleLog.getType().equals(SaleType.REFUND)) {
            return null;
        }
        return saleLog.getSale().getCustomerId();
    }

    default String mapRefundItemIdsToMedicineName(SaleLog saleLog, InventoryRepository inventoryRepository) {
        if (!saleLog.getType().equals(SaleType.REFUND)) {
            return null;
        }
        List<Inventory> inventories = inventoryRepository.findByIdIn(Arrays.asList(saleLog.getRefundItemId().split(",")));
        return String.join(",", inventories.stream().map(inventory -> inventory.getMedicine().getName()).toList());
    }
}
