package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.customerpointconfig.CustomerPointConfigResponse;
import com.app.pharmacy.domain.dto.customerpointconfig.UpdateCustomerPointConfigRequest;
import com.app.pharmacy.domain.entity.CustomerPointConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerPointConfigMapper {
    CustomerPointConfigMapper INSTANCE = Mappers.getMapper(CustomerPointConfigMapper.class);

    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    CustomerPointConfigResponse toResponse(CustomerPointConfig customerPointConfig);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdateCustomerPointConfigRequest request, @MappingTarget CustomerPointConfig customerPointConfig);
}
