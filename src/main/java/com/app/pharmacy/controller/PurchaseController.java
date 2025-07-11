package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.purchase.CreatePurchaseRequest;
import com.app.pharmacy.domain.dto.purchase.CreateUpdatePurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.GetPurchaseRequest;
import com.app.pharmacy.domain.dto.purchase.GetPurchaseResponse;
import com.app.pharmacy.domain.dto.purchase.UpdatePurchaseRequest;
import com.app.pharmacy.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CreateUpdatePurchaseResponse>> createPurchase(
            @Valid @RequestBody CreatePurchaseRequest request, Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(request, connectedUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonGetResponse<GetPurchaseResponse>>> getPurchases(
            @ModelAttribute GetPurchaseRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(purchaseService.getPurchases(request, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CreateUpdatePurchaseResponse>> updatePurchase(
            @PathVariable("id") String purchaseId,
            @Valid @RequestBody UpdatePurchaseRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(purchaseService.updatePurchase(purchaseId, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deletePurchase(@PathVariable("id") String purchaseId) {
        return ResponseEntity.ok(purchaseService.deletePurchase(purchaseId));
    }
}
