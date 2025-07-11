package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.customer.CreateCustomerRequest;
import com.app.pharmacy.domain.dto.customer.CustomerResponse;
import com.app.pharmacy.domain.dto.customer.UpdateCustomerRequest;
import com.app.pharmacy.domain.entity.Customer;
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
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "points", expression = "java(new java.math.BigDecimal(0))")
    Customer toEntity(CreateCustomerRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdateCustomerRequest request, @MappingTarget Customer customer);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    CustomerResponse toCustomerResponse(Customer customer);
    List<CustomerResponse> toListCustomerResponse(List<Customer> customers);
}
