package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.supplier.CreateSupplierRequest;
import com.app.pharmacy.domain.dto.supplier.GetSupplierRequest;
import com.app.pharmacy.domain.dto.supplier.SupplierResponse;
import com.app.pharmacy.domain.dto.supplier.UpdateSupplierRequest;
import com.app.pharmacy.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonGetResponse<SupplierResponse>>> getSuppliers(
            @ModelAttribute GetSupplierRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return ResponseEntity.ok(supplierService.getSuppliers(request, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody CreateSupplierRequest request, Authentication connectedUser
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(request, connectedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable("id") String id, @Valid @RequestBody UpdateSupplierRequest request, Authentication connectedUser
            ){
        return ResponseEntity.ok(supplierService.updateSupplier(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deleteSupplier(@PathVariable("id") String id){
        return ResponseEntity.ok(supplierService.deleteSupplier(id));
    }
}
