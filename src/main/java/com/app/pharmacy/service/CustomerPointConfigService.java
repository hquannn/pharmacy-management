package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.dto.customerpointconfig.CustomerPointConfigResponse;
import com.app.pharmacy.domain.dto.customerpointconfig.UpdateCustomerPointConfigRequest;
import com.app.pharmacy.domain.entity.CustomerPointConfig;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.CustomerPointConfigMapper;
import com.app.pharmacy.repository.CustomerPointConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerPointConfigService {
    private final CustomerPointConfigRepository customerPointConfigRepository;

    public ApiResponse<CustomerPointConfigResponse> getConfig() {
        ApiResponse<CustomerPointConfigResponse> response = new ApiResponse<>();
        CustomerPointConfig customerPointConfig = customerPointConfigRepository.findAll().get(0);

        response.setData(CustomerPointConfigMapper.INSTANCE.toResponse(customerPointConfig));
        return response;
    }

    public ApiResponse<CustomerPointConfigResponse> updateConfig(String id, UpdateCustomerPointConfigRequest request) {
        ApiResponse<CustomerPointConfigResponse> response = new ApiResponse<>();
        customerPointConfigRepository.findById(id).ifPresentOrElse(customerPointConfig -> {
            CustomerPointConfigMapper.INSTANCE.toEntity(request, customerPointConfig);
            customerPointConfigRepository.save(customerPointConfig);
            response.setData(CustomerPointConfigMapper.INSTANCE.toResponse(customerPointConfig));
        }, () -> {
            throw new CustomResponseException(ErrorCode.CONFIG_NOT_EXIST);
        });

        return response;
    }
}
