package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.locationrack.CreateLocationRackRequest;
import com.app.pharmacy.domain.dto.locationrack.GetLocationRackRequest;
import com.app.pharmacy.domain.dto.locationrack.LocationRackResponse;
import com.app.pharmacy.domain.dto.locationrack.UpdateLocationRackRequest;
import com.app.pharmacy.service.LocationRackService;
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
@RequestMapping("/api/v1/locationRacks")
@RequiredArgsConstructor
public class LocationRackController {
    private final LocationRackService locationRackService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<CommonGetResponse<LocationRackResponse>>> getLoactionRacks(
            @ModelAttribute GetLocationRackRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return ResponseEntity.ok(locationRackService.getLocationRack(request, pageable));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LocationRackResponse>> createLocationRack(
            @Valid @RequestBody CreateLocationRackRequest request, Authentication connectedUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationRackService.createLocationRack(request, connectedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LocationRackResponse>> updateLocationRack(
            @PathVariable("id") String id, @Valid @RequestBody UpdateLocationRackRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(locationRackService.updateLocationRack(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommonDeleteResponse>> deleteLocationRack(@PathVariable("id") String id) {
        return ResponseEntity.ok(locationRackService.deleteLocationRack(id));
    }
}
