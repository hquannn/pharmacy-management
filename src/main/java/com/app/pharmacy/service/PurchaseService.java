package com.app.pharmacy.service;
import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.common.Constants;
import com.app.pharmacy.domain.dto.purchase.CreatePurchaseRequest;
import com.app.pharmacy.domain.dto.purchase.CreateUpdatePurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.GetPurchaseRequest;
import com.app.pharmacy.domain.dto.purchase.GetPurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.UpdatePurchaseRequest;
import com.app.pharmacy.domain.entity.Inventory;
import com.app.pharmacy.domain.entity.Medicine;
import com.app.pharmacy.domain.entity.Purchase;
import com.app.pharmacy.domain.entity.Supplier;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.PurchaseMapper;
import com.app.pharmacy.repository.InventoryRepository;
import com.app.pharmacy.repository.MedicineRepository;
import com.app.pharmacy.repository.PurchaseRepository;
import com.app.pharmacy.repository.SupplierRepository;
import com.app.pharmacy.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.app.pharmacy.specification.PurchaseSpecification.hasPurchaseDate;
import static com.app.pharmacy.specification.PurchaseSpecification.hasMedicineName;
import static com.app.pharmacy.specification.PurchaseSpecification.hasSupplierName;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final InventoryRepository inventoryRepository;
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;
    private final Clock clock;

    public ApiResponse<CreateUpdatePurchaseResponse> createPurchase(CreatePurchaseRequest request, Authentication connectedUser) {
        ApiResponse<CreateUpdatePurchaseResponse> response = new ApiResponse<>();

        Medicine medicine = medicineRepository.findById(request.medicineId())
                .orElseThrow(() -> new CustomResponseException(ErrorCode.MEDICINE_NOT_EXIST));

        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new CustomResponseException(ErrorCode.SUPPLIER_NOT_EXIST));
        Purchase purchase = PurchaseMapper.INSTANCE.toEntity(request);
        purchase.setMedicine(medicine);
        purchase.setSupplier(supplier);
        purchase.setCreatedBy(connectedUser.getName());
        purchase.setCreatedDate(LocalDateTime.now(clock));
        purchase.setCode(Constants.PURCHASE_CODE_PREFIX
                + StringUtils.generateRandomString(2)
                + StringUtils.generateRandomNumberString(6));

        saveInventory(request, connectedUser, medicine);

        purchaseRepository.save(purchase);

        CreateUpdatePurchaseResponse purchaseResponse = PurchaseMapper.INSTANCE.toCreateUpdateResponse(purchase);
        response.setData(purchaseResponse);
        return response;
    }

    private void saveInventory(CreatePurchaseRequest request, Authentication connectedUser, Medicine medicine) {
        Optional<Inventory> inventory = inventoryRepository.findByMedicineIdAndMfgDate(request.medicineId(), request.mfgDate());
        inventory.ifPresentOrElse(i -> {
                    i.setQuantity(i.getQuantity() + request.quantity());
                    i.setUpdatedBy(connectedUser.getName());
                    i.setUpdatedDate(LocalDateTime.now(clock));
                    inventoryRepository.save(i);
                }, () -> inventoryRepository.save(Inventory
                        .builder()
                        .medicine(medicine)
                        .locationRackId(request.locationRackId())
                        .quantity(request.quantity())
                        .mfgDate(request.mfgDate())
                        .expDate(request.expDate())
                        .createdBy(connectedUser.getName())
                        .createdDate(LocalDateTime.now(clock))
                        .build())
        );
    }

    public ApiResponse<CommonGetResponse<GetPurchaseResponse>> getPurchases(GetPurchaseRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<GetPurchaseResponse>> response = new ApiResponse<>();
        Specification<Purchase> specification = Specification.where(
                hasMedicineName(request.medicineName())
                        .and(hasSupplierName(request.supplierName()))
                        .and(hasPurchaseDate(request.purDateBegin(), request.purDateEnd())));

        Page<Purchase> purchasePage = purchaseRepository.findAll(specification, pageable);
        List<GetPurchaseResponse> getPurchaseResponses = PurchaseMapper.INSTANCE.toPurchaseList(purchasePage.getContent());
        response.setData(new CommonGetResponse<>(
                getPurchaseResponses,
                purchasePage.getSize(),
                purchasePage.getNumber(),
                purchasePage.getTotalElements()));
        return response;
    }

    public ApiResponse<CreateUpdatePurchaseResponse> updatePurchase(
            String purchaseId, UpdatePurchaseRequest request, Authentication connectedUser) {
        ApiResponse<CreateUpdatePurchaseResponse> response = new ApiResponse<>();
        purchaseRepository.findById(purchaseId).ifPresentOrElse(purchase -> {

            if (!Objects.equals(request.medicineId(), purchase.getMedicine().getId()) && request.medicineId() != null
                    && (request.mfgDate() == null || request.expDate() == null || request.locationRackId() == null)) {
                throw new CustomResponseException(ErrorCode.PURCHASE_UPDATE_MEDICINE_INFO_REQUIRED);
            }
            if (request.medicineId() != null && !Objects.equals(request.medicineId(), purchase.getMedicine().getId())) {
                // decrease quantity in inventory if exist
                Optional<Inventory> inventory = inventoryRepository.findByMedicineIdAndMfgDate(
                        purchase.getMedicine().getId(), request.mfgDate());
                inventory.ifPresentOrElse(i -> {
                    i.setQuantity(i.getQuantity() - request.quantity());
                    inventoryRepository.save(i);
                }, () -> {});

                medicineRepository.findById(request.medicineId()).ifPresentOrElse(purchase::setMedicine, () -> {
                    throw new CustomResponseException(ErrorCode.MEDICINE_NOT_EXIST);
                });
                saveInventory(PurchaseMapper.INSTANCE.toCreateRequest(request), connectedUser, purchase.getMedicine());

            }
            if (request.supplierId() != null) {
                supplierRepository.findById(request.supplierId()).ifPresentOrElse(purchase::setSupplier, () -> {
                    throw new CustomResponseException(ErrorCode.SUPPLIER_NOT_EXIST);
                });
            }
            PurchaseMapper.INSTANCE.toEntity(request, purchase);
            purchaseRepository.save(purchase);

            CreateUpdatePurchaseResponse purchaseResponse = PurchaseMapper.INSTANCE.toCreateUpdateResponse(purchase);
            response.setData(purchaseResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.PURCHASE_NOT_EXIST);
        });

        return response;
    }

    public ApiResponse<CommonDeleteResponse> deletePurchase(String purchaseId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        purchaseRepository.findById(purchaseId).ifPresentOrElse(purchaseRepository::delete, () -> {
            throw new CustomResponseException(ErrorCode.PURCHASE_NOT_EXIST);
        });

        response.setData(new CommonDeleteResponse(purchaseId));
        return response;
    }
}
