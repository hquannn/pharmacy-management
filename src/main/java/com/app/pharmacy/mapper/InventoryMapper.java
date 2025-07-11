package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.inventory.InventoryDTO;
import com.app.pharmacy.domain.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    @Mapping(target = "isGettingExpire", source = "expDate", qualifiedByName = "isGettingExpire")
    InventoryDTO toDto(Inventory inventory);

    List<InventoryDTO> toDtos(List<Inventory> inventories);

    @Named("isGettingExpire")
    default Boolean isGettingExpire(LocalDate expDate) {
        return LocalDate.now().isAfter(expDate.minusMonths(7));
    }
}
