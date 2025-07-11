package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.customer.CreateCustomerRequest;
import com.app.pharmacy.domain.dto.customer.GetCustomerRequest;
import com.app.pharmacy.domain.dto.customer.CustomerResponse;
import com.app.pharmacy.domain.dto.customer.UpdateCustomerRequest;
import com.app.pharmacy.domain.entity.Customer;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.CustomerMapper;
import com.app.pharmacy.repository.CustomerRepository;
import com.app.pharmacy.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.CustomerSpecification.hasFirstName;
import static com.app.pharmacy.specification.CustomerSpecification.hasLastName;
import static com.app.pharmacy.specification.CustomerSpecification.hasPhoneNo;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final Clock clock;

    public ApiResponse<CustomerResponse> createCustomer(CreateCustomerRequest request, Authentication connectedUser) {
        ApiResponse<CustomerResponse> response = new ApiResponse<>();
        Customer customer = CustomerMapper.INSTANCE.toEntity(request);
        customer.setCreatedBy(connectedUser.getName());
        customer.setCreatedDate(LocalDateTime.now(clock));
        try {
            customerRepository.save(customer);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("ERROR: duplicate key value violates unique constraint")) {
                throw new CustomResponseException(ErrorCode.PHONE_NO_EXISTED);
            }
        }

        CustomerResponse customerResponse = CustomerMapper.INSTANCE.toCustomerResponse(customer);
        response.setData(customerResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<CustomerResponse>> getCustomers(GetCustomerRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<CustomerResponse>> response = new ApiResponse<>();

        Specification<Customer> specification = Specification.where(
                (hasFirstName(request.name()).or(hasLastName(request.name()))).and(hasPhoneNo(request.phoneNo())));
        Page<Customer> customersPage = customerRepository.findAll(specification, pageable);
        List<CustomerResponse> customerResponses = CustomerMapper.INSTANCE.toListCustomerResponse(customersPage.getContent());
        response.setData(new CommonGetResponse<>(
                customerResponses,
                customersPage.getSize(),
                customersPage.getNumber(),
                customersPage.getTotalElements()));
        return response;
    }

    public ApiResponse<CustomerResponse> updateCustomer(
            String customerId, UpdateCustomerRequest request, Authentication connectedUser) {
        ApiResponse<CustomerResponse> response = new ApiResponse<>();
        customerRepository.findById(customerId).ifPresentOrElse(customer -> {
            CustomerMapper.INSTANCE.toEntity(request, customer);
            customer.setUpdatedBy(connectedUser.getName());
            customer.setUpdatedDate(LocalDateTime.now(clock));
            customerRepository.save(customer);

            CustomerResponse customerResponse = CustomerMapper.INSTANCE.toCustomerResponse(customer);
            response.setData(customerResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.CUSTOMER_NOT_EXIST);
        });

        return response;
    }

    public ApiResponse<CommonDeleteResponse> deleteCustomer(String customerId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        customerRepository.findById(customerId).ifPresentOrElse(customer -> {
            if (saleRepository.existsByCustomerId(customerId)) {
                throw new CustomResponseException(ErrorCode.CUSTOMER_IS_BEING_USED);
            }
            customerRepository.delete(customer);
        }, () -> {
            throw new CustomResponseException(ErrorCode.CUSTOMER_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(customerId));
        return response;
    }

}
