package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.employee.ChangePasswordRequest;
import com.app.pharmacy.domain.dto.employee.CreateEmployeeRequest;
import com.app.pharmacy.domain.dto.employee.EmployeeResponse;
import com.app.pharmacy.domain.dto.employee.GetEmployeeRequest;
import com.app.pharmacy.domain.dto.employee.UpdateEmployeeRequest;
import com.app.pharmacy.service.EmployeeService;
import com.app.pharmacy.service.KeycloakAdminService;
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
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final KeycloakAdminService keycloakAdminService;
    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request, Authentication connectedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request, connectedUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonGetResponse<EmployeeResponse>>> getEmployees(
            @ModelAttribute GetEmployeeRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(employeeService.getEmployees(request, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable("id") String id, @Valid @RequestBody UpdateEmployeeRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deleteEmployee(@PathVariable("id") String id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(employeeService.changePassword(request, connectedUser));
    }
}
