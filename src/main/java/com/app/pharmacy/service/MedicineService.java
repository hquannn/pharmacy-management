package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.medicine.CreateMedicineRequest;
import com.app.pharmacy.domain.dto.medicine.GetMedicineRequest;
import com.app.pharmacy.domain.dto.medicine.MedUnitResponse;
import com.app.pharmacy.domain.dto.medicine.MedicineResponse;
import com.app.pharmacy.domain.dto.medicine.UpdateMedicineRequest;
import com.app.pharmacy.domain.entity.Medicine;
import com.app.pharmacy.domain.entity.MedicineUnit;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.MedicineMapper;
import com.app.pharmacy.repository.MedicineCategoryRepository;
import com.app.pharmacy.repository.MedicineRepository;
import com.app.pharmacy.repository.MedicineUnitRepository;
import com.app.pharmacy.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.MedicineSpecification.hasCategory;
import static com.app.pharmacy.specification.MedicineSpecification.hasName;
import static com.app.pharmacy.specification.MedicineSpecification.hasQuantity;

import static com.app.pharmacy.specification.MedicineSpecification.*;

@Service
@RequiredArgsConstructor
public class MedicineService  {
    private final MedicineRepository medicineRepository;
    private final MedicineCategoryRepository medicineCategoryRepository;
    private final MedicineUnitRepository medicineUnitRepository;
    private final PurchaseRepository purchaseRepository;
    private final Clock clock;

    public ApiResponse<CommonGetResponse<MedicineResponse>> getMedicines(GetMedicineRequest request, Pageable pageable){
        ApiResponse<CommonGetResponse<MedicineResponse>> response = new ApiResponse<>();

        Specification<Medicine> specification = Specification.where(
                hasName(request.name())
                        .and(hasCategory(request.categoryId()))
                                .and(hasQuantity(request.quantity())));
        Page<Medicine> medicinePage = medicineRepository.findAll(specification, pageable);
        List<MedicineResponse> medicineResponses = MedicineMapper.INSTANCE.toListMedicineResponse(medicinePage.getContent());
        response.setData(new CommonGetResponse<>(
                medicineResponses,
                medicinePage.getSize(),
                medicinePage.getNumber(),
                medicinePage.getTotalElements()
        ));
        return response;
    }
    public ApiResponse<MedicineResponse> createMedicine(CreateMedicineRequest request, Authentication connectedUser){        ApiResponse<MedicineResponse> response = new ApiResponse<>();
        if (medicineCategoryRepository.findById(request.categoryId()).isEmpty()) {
            throw new CustomResponseException(ErrorCode.CATEGORY_NOT_EXIST);
        }

        if (medicineUnitRepository.findById(request.medicineUnitId()).isEmpty()) {
            throw new CustomResponseException(ErrorCode.UNIT_NOT_EXIST);
        }

        Medicine medicine = MedicineMapper.INSTANCE.toEntity(request);
        medicine.setCreatedBy(connectedUser.getName());
        medicine.setCreatedDate(LocalDateTime.now(clock));
        medicineRepository.save(medicine);

        MedicineResponse medicineResponse = MedicineMapper.INSTANCE.toMedicineResponse(medicine);
        response.setData(medicineResponse);
        return response;
    }
    public ApiResponse<MedicineResponse> updateMedicine(
            String medicineId, UpdateMedicineRequest request, Authentication connectedUser) {
        ApiResponse<MedicineResponse> response = new ApiResponse<>();
        medicineRepository.findById(medicineId).ifPresentOrElse(medicine -> {
            if (request.medicineUnitId() != null && medicineUnitRepository.findById(request.medicineUnitId()).isEmpty()) {
                throw new CustomResponseException(ErrorCode.UNIT_NOT_EXIST);
            }
            MedicineMapper.INSTANCE.toEntity(request, medicine);
            medicine.setUpdatedBy(connectedUser.getName());
            medicine.setUpdatedDate(LocalDateTime.now(clock));
            medicineRepository.save(medicine);

            MedicineResponse medicineResponse = MedicineMapper.INSTANCE.toMedicineResponse(medicine);
            response.setData(medicineResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.MEDICINE_NOT_EXIST);
        });

        return response;
    }

    public ApiResponse<CommonDeleteResponse> deleteMedicine(String medicineId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        medicineRepository.findById(medicineId).ifPresentOrElse(medicine  -> {
            if (purchaseRepository.existsByMedicineId(medicine.getId())) {
                throw new CustomResponseException(ErrorCode.MED_IS_BEING_USED);
            }
            medicineRepository.delete(medicine);
        }, (

        ) -> {
            throw new CustomResponseException(ErrorCode.MEDICINE_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(medicineId));
        return response;
    }
    public ApiResponse<List<MedUnitResponse>> getMedicineUnits() {
        ApiResponse<List<MedUnitResponse>> response = new ApiResponse<>();
        List<MedicineUnit> medicineUnits = medicineUnitRepository.findAll();
        response.setData(MedicineMapper.INSTANCE.toListMedUnitResponse(medicineUnits));
        return response;
    }
}
