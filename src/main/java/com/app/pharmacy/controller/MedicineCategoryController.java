package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.medicinecategory.CreateMedicineCategoryRequest;
import com.app.pharmacy.domain.dto.medicinecategory.GetMedicineCategoryRequest;
import com.app.pharmacy.domain.dto.medicinecategory.MedicineCategoryResponse;
import com.app.pharmacy.domain.dto.medicinecategory.UpdateMedicineCategoryRequest;
import com.app.pharmacy.service.MedicineCategoryService;
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
@RequestMapping("/api/v1/medicineCategories")
@RequiredArgsConstructor
public class MedicineCategoryController {
    private final MedicineCategoryService medicineCategoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<CommonGetResponse<MedicineCategoryResponse>>> getMedicineCategories(
            @ModelAttribute GetMedicineCategoryRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(medicineCategoryService.getMedicineCategories(request, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineCategoryResponse>> createMedicineCategory(
            @Valid @RequestBody CreateMedicineCategoryRequest request, Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineCategoryService.createMedicineCategory(request, connectedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineCategoryResponse>> updateMedicineCategory(
            @PathVariable("id") String id, @Valid @RequestBody UpdateMedicineCategoryRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(medicineCategoryService.updateMedicineCategory(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deleteMedicineCategory(@PathVariable("id") String id) {
        return ResponseEntity.ok(medicineCategoryService.deleteMedicineCategory(id));
    }
}
