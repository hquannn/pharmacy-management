package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.supplier.CreateSupplierRequest;
import com.app.pharmacy.domain.dto.supplier.SupplierResponse;
import com.app.pharmacy.domain.dto.supplier.UpdateSupplierRequest;
import com.app.pharmacy.domain.entity.Supplier;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {
    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    Supplier toEntity(CreateSupplierRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdateSupplierRequest request, @MappingTarget Supplier supplier);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    SupplierResponse toSupplierResponse(Supplier supplier);
    List<SupplierResponse> toListSuppierResponse(List<Supplier> suppliers);
}
