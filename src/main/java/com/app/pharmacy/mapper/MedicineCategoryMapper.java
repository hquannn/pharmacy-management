package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.medicinecategory.CreateMedicineCategoryRequest;
import com.app.pharmacy.domain.dto.medicinecategory.MedicineCategoryResponse;
import com.app.pharmacy.domain.dto.medicinecategory.UpdateMedicineCategoryRequest;
import com.app.pharmacy.domain.entity.MedicineCategory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicineCategoryMapper {
    //get instance singleton of mapper that mapstruct generate
    MedicineCategoryMapper INSTANCE = Mappers.getMapper(MedicineCategoryMapper.class);
    //mapping from DTO to entity
    MedicineCategory toEntity(CreateMedicineCategoryRequest request);
    //Mapping update(update an existing entity from DTO update)
    //use for update function(only update valued field
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UpdateMedicineCategoryRequest request, @MappingTarget MedicineCategory medicineCategory);
    //mapping from entity to response DTO
    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "employeeUpdated.firstName")
    MedicineCategoryResponse toMedicineCategoryResponse(MedicineCategory medicineCategory);
    List<MedicineCategoryResponse> toListMedicineCategoryResponse(List<MedicineCategory> medicineCategories);
}