package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.supplier.CreateSupplierRequest;
import com.app.pharmacy.domain.dto.supplier.GetSupplierRequest;
import com.app.pharmacy.domain.dto.supplier.SupplierResponse;
import com.app.pharmacy.domain.dto.supplier.UpdateSupplierRequest;
import com.app.pharmacy.domain.entity.Supplier;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.SupplierMapper;
import com.app.pharmacy.repository.PurchaseRepository;
import com.app.pharmacy.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.SupplierSpecification.hasName;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final Clock clock;

    public ApiResponse<SupplierResponse> createSupplier(CreateSupplierRequest request, Authentication connectedUser) {
        ApiResponse<SupplierResponse> response = new ApiResponse<>();
        Supplier supplier = SupplierMapper.INSTANCE.toEntity(request);
        supplier.setCreatedBy(connectedUser.getName());
        supplier.setCreatedDate(LocalDateTime.now(clock));
        supplierRepository.save(supplier);

        SupplierResponse supplierResponse = SupplierMapper.INSTANCE.toSupplierResponse(supplier);
        response.setData(supplierResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<SupplierResponse>> getSuppliers(GetSupplierRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<SupplierResponse>> response = new ApiResponse<>();
        Specification<Supplier> specification = Specification.where(
                hasName(request.name()));
        Page<Supplier> supplierPage = supplierRepository.findAll(specification, pageable);
        List<SupplierResponse> supplierResponses = SupplierMapper.INSTANCE.toListSuppierResponse(supplierPage.getContent());
        response.setData(new CommonGetResponse<>(
                supplierResponses,
                supplierPage.getSize(),
                supplierPage.getNumber(),
                supplierPage.getTotalElements()));
        return response;
    }
    public ApiResponse<SupplierResponse> updateSupplier(String supplierId, UpdateSupplierRequest request, Authentication connectedUser){
        ApiResponse<SupplierResponse> response = new ApiResponse<>();
        supplierRepository.findById(supplierId).ifPresentOrElse(supplier -> {
            SupplierMapper.INSTANCE.toEntity(request, supplier);
            supplier.setUpdatedBy(connectedUser.getName());
            supplier.setCreatedDate(LocalDateTime.now(clock));
            supplierRepository.save(supplier);

            SupplierResponse supplierResponse = SupplierMapper.INSTANCE.toSupplierResponse(supplier);
            response.setData(supplierResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.CUSTOMER_NOT_EXIST);
        });
        return response;
    }
    public ApiResponse<CommonDeleteResponse> deleteSupplier(String supplierId){
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        supplierRepository.findById(supplierId).ifPresentOrElse(supplier -> {
            if (purchaseRepository.existsBySupplierId(supplierId)) {
                throw new CustomResponseException(ErrorCode.SUPPLIER_IS_BEING_USED);
            }
            supplierRepository.delete(supplier);
        }, () -> {
            throw new CustomResponseException(ErrorCode.SUPPLIER_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(supplierId));
        return response;
    }
}

