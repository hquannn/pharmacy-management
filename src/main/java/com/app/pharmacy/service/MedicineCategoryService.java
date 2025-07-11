package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.medicinecategory.CreateMedicineCategoryRequest;
import com.app.pharmacy.domain.dto.medicinecategory.GetMedicineCategoryRequest;
import com.app.pharmacy.domain.dto.medicinecategory.MedicineCategoryResponse;
import com.app.pharmacy.domain.dto.medicinecategory.UpdateMedicineCategoryRequest;
import com.app.pharmacy.domain.entity.MedicineCategory;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.MedicineCategoryMapper;
import com.app.pharmacy.repository.MedicineCategoryRepository;
import com.app.pharmacy.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.MedicineCategorySpecification.hasName;

@Service
@RequiredArgsConstructor
public class MedicineCategoryService {
    private final MedicineCategoryRepository medicinecategoryRepository;
    private final MedicineRepository medicineRepository;
    private final Clock clock;

    public ApiResponse<MedicineCategoryResponse> createMedicineCategory(CreateMedicineCategoryRequest request, Authentication connectedUser) {
        ApiResponse<MedicineCategoryResponse> response = new ApiResponse<>();
        MedicineCategory medicinecategory = MedicineCategoryMapper.INSTANCE.toEntity(request);
        medicinecategory.setCreatedBy(connectedUser.getName());
        medicinecategory.setCreatedDate(LocalDateTime.now(clock));
        medicinecategoryRepository.save(medicinecategory);

        MedicineCategoryResponse medicinecategoryResponse = MedicineCategoryMapper.INSTANCE.toMedicineCategoryResponse(medicinecategory);
        response.setData(medicinecategoryResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<MedicineCategoryResponse>> getMedicineCategories(GetMedicineCategoryRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<MedicineCategoryResponse>> response = new ApiResponse<>();

        Specification<MedicineCategory> specification = Specification.where(
                hasName(request.name()));
        Page<MedicineCategory> medicinecategorysPage = medicinecategoryRepository.findAll(specification, pageable);
        List<MedicineCategoryResponse> medicineCategoryResponses = MedicineCategoryMapper.INSTANCE.toListMedicineCategoryResponse(medicinecategorysPage.getContent());
        response.setData(new CommonGetResponse<>(
                medicineCategoryResponses,
                medicinecategorysPage.getSize(),
                medicinecategorysPage.getNumber(),
                medicinecategorysPage.getTotalElements()));
        return response;
    }

    public ApiResponse<MedicineCategoryResponse> updateMedicineCategory(
            String medicineCategoryId, UpdateMedicineCategoryRequest request, Authentication connectedUser) {
        ApiResponse<MedicineCategoryResponse> response = new ApiResponse<>();
        medicinecategoryRepository.findById(medicineCategoryId).ifPresentOrElse(medicinecategory -> {
            MedicineCategoryMapper.INSTANCE.toEntity(request, medicinecategory);
            medicinecategory.setUpdatedBy(connectedUser.getName());
            medicinecategory.setUpdatedDate(LocalDateTime.now(clock));
            medicinecategoryRepository.save(medicinecategory);

            MedicineCategoryResponse medicinecategoryResponse = MedicineCategoryMapper.INSTANCE.toMedicineCategoryResponse(medicinecategory);
            response.setData(medicinecategoryResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.CATEGORY_NOT_EXIST);
        });

        return response;
    }

    public ApiResponse<CommonDeleteResponse> deleteMedicineCategory(String medicineCategoryId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        medicinecategoryRepository.findById(medicineCategoryId).ifPresentOrElse(medicineCategory -> {
            if (medicineRepository.existsByCategoryId(medicineCategoryId)) {
                throw new CustomResponseException(ErrorCode.CATEGORY_IS_BEING_USED);
            }
            medicinecategoryRepository.delete(medicineCategory);
        }, () -> {
            throw new CustomResponseException(ErrorCode.CATEGORY_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(medicineCategoryId));
        return response;
    }
}
