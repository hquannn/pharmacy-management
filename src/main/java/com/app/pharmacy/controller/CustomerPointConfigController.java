package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.dto.customerpointconfig.CustomerPointConfigResponse;
import com.app.pharmacy.domain.dto.customerpointconfig.UpdateCustomerPointConfigRequest;
import com.app.pharmacy.service.CustomerPointConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customerPointConfigs")
@RequiredArgsConstructor
public class CustomerPointConfigController {
    private final CustomerPointConfigService customerPointConfigService;

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerPointConfigResponse>> getConfig() {
        return ResponseEntity.ok(customerPointConfigService.getConfig());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerPointConfigResponse>> updateConfig(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateCustomerPointConfigRequest request) {
        return ResponseEntity.ok(customerPointConfigService.updateConfig(id, request));
    }
}
