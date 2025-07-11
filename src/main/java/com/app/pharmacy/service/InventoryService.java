package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.inventory.GetInventoryRequest;
import com.app.pharmacy.domain.dto.inventory.InventoryDTO;

import com.app.pharmacy.domain.entity.Inventory;
import com.app.pharmacy.mapper.InventoryMapper;
import com.app.pharmacy.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.app.pharmacy.specification.InventorySpecification.hasExpireDate;
import static com.app.pharmacy.specification.InventorySpecification.hasMedicineName;
import static com.app.pharmacy.specification.InventorySpecification.hasQuantity;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public ApiResponse<CommonGetResponse<InventoryDTO>> getInventory(GetInventoryRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<InventoryDTO>> response = new ApiResponse<>();
        Specification<Inventory> specification = Specification.where(
                hasMedicineName(request.medicineName())
                        .and(hasQuantity(request.quantity()))
                        .and(hasExpireDate(request.expireDateBegin(), request.expireDateEnd()))
        );
        Page<Inventory> inventoryPage = inventoryRepository.findAll(specification, pageable);
        List<InventoryDTO> inventoryDtos = InventoryMapper.INSTANCE.toDtos(inventoryPage.getContent());
        response.setData(new CommonGetResponse<>(
                inventoryDtos,
                inventoryPage.getSize(),
                inventoryPage.getNumber(),
                inventoryPage.getTotalElements()
        ));
        return response;
    }
}
