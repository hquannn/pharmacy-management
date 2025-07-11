package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.medicine.MedicineResponse;
import com.app.pharmacy.domain.dto.purchase.CreatePurchaseRequest;
import com.app.pharmacy.domain.dto.purchase.CreateUpdatePurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.GetPurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.UpdatePurchaseRequest;
import com.app.pharmacy.domain.dto.supplier.SupplierResponse;
import com.app.pharmacy.domain.entity.Medicine;
import com.app.pharmacy.domain.entity.Purchase;
import com.app.pharmacy.domain.entity.Supplier;
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
public interface PurchaseMapper {
    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    Purchase toEntity(CreatePurchaseRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdatePurchaseRequest request, @MappingTarget Purchase purchase);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    MedicineResponse toMedicineResponse(Medicine medicine);
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    SupplierResponse toMedicineResponse(Supplier medicine);
    CreateUpdatePurchaseResponse toCreateUpdateResponse(Purchase purchase);

    @Mapping(target = "medicine", source = "medicine")
    @Mapping(target = "medicine.createdBy", source = "medicine.employeeCreated.firstName")
    @Mapping(target = "medicine.updatedBy", source = "medicine.employeeUpdated.firstName")
    @Mapping(target = "supplier", source = "supplier")
    @Mapping(target = "supplier.createdBy", source = "supplier.employeeCreated.firstName")
    @Mapping(target = "supplier.updatedBy", source = "supplier.employeeUpdated.firstName")
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    GetPurchaseResponse toGetPurchaseResponse(Purchase purchase);

    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    List<GetPurchaseResponse> toPurchaseList(List<Purchase> purchases);

    CreatePurchaseRequest toCreateRequest(UpdatePurchaseRequest request);
}
