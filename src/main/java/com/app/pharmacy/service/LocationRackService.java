package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.locationrack.CreateLocationRackRequest;
import com.app.pharmacy.domain.dto.locationrack.GetLocationRackRequest;
import com.app.pharmacy.domain.dto.locationrack.LocationRackResponse;
import com.app.pharmacy.domain.dto.locationrack.UpdateLocationRackRequest;
import com.app.pharmacy.domain.entity.LocationRack;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.LocationRackMapper;
import com.app.pharmacy.repository.InventoryRepository;
import com.app.pharmacy.repository.LocationRackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.LocationRackSpecification.hasPosition;

@Service
@RequiredArgsConstructor
public class LocationRackService {
    private final LocationRackRepository locationRackRepository;
    private final InventoryRepository inventoryRepository;
    private final Clock  clock;

    public ApiResponse<LocationRackResponse> createLocationRack(CreateLocationRackRequest request, Authentication connectedUser){
        ApiResponse<LocationRackResponse> response = new ApiResponse<>();
        LocationRack locationRack = LocationRackMapper.INSTANCE.toEntity(request);
        locationRack.setCreatedBy(connectedUser.getName());
        locationRack.setCreatedDate(LocalDateTime.now(clock));
        locationRackRepository.save(locationRack);

        LocationRackResponse locationRackResponse = LocationRackMapper.INSTANCE.toLocationRackResponse(locationRack);
        response.setData(locationRackResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<LocationRackResponse>> getLocationRack(GetLocationRackRequest request, Pageable pageable){
        ApiResponse<CommonGetResponse<LocationRackResponse>> response = new ApiResponse<>();
        Specification<LocationRack> specification = Specification.where(hasPosition(request.position()));
        Page<LocationRack> locationRacksPage = locationRackRepository.findAll(specification, pageable);
        List<LocationRackResponse> locationRackResponses = LocationRackMapper.INSTANCE.toListLocationRackResponse(locationRacksPage.getContent());
        response.setData(new CommonGetResponse<>(
                locationRackResponses,
                locationRacksPage.getSize(),
                locationRacksPage.getNumber(),
                locationRacksPage.getTotalElements()));
        return response;
    }
    public ApiResponse<LocationRackResponse> updateLocationRack(String locationRackId, UpdateLocationRackRequest request, Authentication connectedUser){
        ApiResponse<LocationRackResponse> response = new ApiResponse<>();
        locationRackRepository.findById(locationRackId).ifPresentOrElse(locationrack -> {
            LocationRackMapper.INSTANCE.toEntity(request, locationrack);
            locationrack.setUpdatedBy(connectedUser.getName());
            locationrack.setUpdatedDate(LocalDateTime.now(clock));
            locationRackRepository.save(locationrack);

            LocationRackResponse locationrackResponse = LocationRackMapper.INSTANCE.toLocationRackResponse(locationrack);
            response.setData(locationrackResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.LOCATION_RACK_NOT_EXIST);
        });
        return response;
    }

    public ApiResponse<CommonDeleteResponse> deleteLocationRack(String locationRackId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        locationRackRepository.findById(locationRackId).ifPresentOrElse(locationRack -> {
            if (inventoryRepository.existsByLocationRackId(locationRackId)) {
                throw new CustomResponseException(ErrorCode.LOCATION_RACK_IS_BEING_USED);
            }
            locationRackRepository.delete(locationRack);
        }, () -> {
            throw new CustomResponseException(ErrorCode.LOCATION_RACK_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(locationRackId));
        return response;
    }

}
