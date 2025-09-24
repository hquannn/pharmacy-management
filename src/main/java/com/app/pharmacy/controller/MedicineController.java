package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.medicine.*;
import com.app.pharmacy.service.MedicineService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN') ||  hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CommonGetResponse<MedicineResponse>>> getMedicines(
            @ModelAttribute GetMedicineRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(medicineService.getMedicines(request, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
            @Valid @RequestBody CreateMedicineRequest request, Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.createMedicine(request, connectedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(
            @PathVariable("id") String id, @Valid @RequestBody UpdateMedicineRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deleteMedicine(@PathVariable("id") String id) {
        return ResponseEntity.ok(medicineService.deleteMedicine(id));
    }

    @GetMapping("/units")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN') || hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<MedUnitResponse>>> getMedicineUnit() {
        return ResponseEntity.ok(medicineService.getMedicineUnits());
    }

    @PostMapping("/scan")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<ApiResponse<MedicineScanResponse>> processMedicineImage(
            @RequestParam("file") MultipartFile file) {
        ApiResponse<MedicineScanResponse> response = medicineService.processingMedImg(file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/extract")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<ApiResponse<MedicineExtractResponse>> processImage(
            @RequestParam("file") MultipartFile file) {
        ApiResponse<MedicineExtractResponse> response = medicineService.processImg(file);
        return ResponseEntity.ok(response);
    }
}
