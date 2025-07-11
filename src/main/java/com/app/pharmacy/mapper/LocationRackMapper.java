package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.locationrack.CreateLocationRackRequest;
import com.app.pharmacy.domain.dto.locationrack.LocationRackResponse;
import com.app.pharmacy.domain.dto.locationrack.UpdateLocationRackRequest;
import com.app.pharmacy.domain.entity.LocationRack;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationRackMapper {
    LocationRackMapper INSTANCE = Mappers.getMapper(LocationRackMapper.class);

    LocationRack toEntity(CreateLocationRackRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdateLocationRackRequest request, @MappingTarget LocationRack locationrack);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    LocationRackResponse toLocationRackResponse(LocationRack locationrack);
    List<LocationRackResponse> toListLocationRackResponse(List<LocationRack> locationRacks);
}
