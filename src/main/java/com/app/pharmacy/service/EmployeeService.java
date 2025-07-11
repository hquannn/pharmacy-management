package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonDeleteResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.employee.*;
import com.app.pharmacy.domain.entity.Employee;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.EmployeeMapper;
import com.app.pharmacy.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.app.pharmacy.specification.EmployeeSpecification.hasFirstName;
import static com.app.pharmacy.specification.EmployeeSpecification.hasLastName;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final KeycloakAdminService keycloakAdminService;
    private final Clock clock;
    private final EmployeeRepository employeeRepository;

    public ApiResponse<EmployeeResponse> createEmployee(
            CreateEmployeeRequest request, Authentication connectedUser) {

        ApiResponse<EmployeeResponse> response = new ApiResponse<>();
        String userId = keycloakAdminService.createUser(request);
        Employee employee = EmployeeMapper.INSTANCE.toEntity(request);
        employee.setId(userId);
        employee.setJoinDate(LocalDateTime.now(clock));
        employee.setCreatedBy(connectedUser.getName());
        employee.setCreatedDate(LocalDateTime.now(clock));
        employeeRepository.save(employee);

        EmployeeResponse employeeResponse = EmployeeMapper.INSTANCE.toEmployeeResponse(employee);
        employeeResponse.setUsername(request.username());
        employeeResponse.setRole(request.role());
        response.setData(employeeResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<EmployeeResponse>> getEmployees(GetEmployeeRequest request, Pageable pageable) {
        ApiResponse<CommonGetResponse<EmployeeResponse>> response = new ApiResponse<>();

        Specification<Employee> specification = Specification.where(hasFirstName(request.name()).or(hasLastName(request.name())));
        Page<Employee> employees = employeeRepository.findAll(specification, pageable);
        List<EmployeeResponse> employeeResponses = EmployeeMapper.INSTANCE.toListEmployeeResponse(employees.getContent());

        response.setData(new CommonGetResponse<>(
                employeeResponses,
                employees.getSize(),
                employees.getNumber(),
                employees.getTotalElements()));

        return response;
    }

    public ApiResponse<EmployeeResponse> updateEmployee(
            String employeeId, UpdateEmployeeRequest request, Authentication connectedUser) {
        ApiResponse<EmployeeResponse> response = new ApiResponse<>();
        employeeRepository.findById(employeeId).ifPresentOrElse(employee -> {
            if (!request.username().equals(employee.getUsername())) {
                throw new CustomResponseException(ErrorCode.USERNAME_CANNOT_CHANGE);
            }
            EmployeeMapper.INSTANCE.toEntity(request, employee);
            employee.setUpdatedBy(connectedUser.getName());
            employee.setUpdatedDate(LocalDateTime.now(clock));
            employeeRepository.save(employee);

            EmployeeResponse employeeResponse = EmployeeMapper.INSTANCE.toEmployeeResponse(employee);
            employeeResponse.setUsername(request.username());
            response.setData(employeeResponse);
        }, () -> {
            throw new CustomResponseException(ErrorCode.USER_NOT_EXIST);
        });

        return response;
    }

    public ApiResponse<CommonDeleteResponse> deleteEmployee(String employeeId) {
        ApiResponse<CommonDeleteResponse> response = new ApiResponse<>();
        keycloakAdminService.deleteUser(employeeId);
        employeeRepository.findById(employeeId).ifPresentOrElse(employeeRepository::delete, () -> {
            throw new CustomResponseException(ErrorCode.USER_NOT_EXIST);
        });
        response.setData(new CommonDeleteResponse(employeeId));
        return response;
    }

    public ApiResponse<Boolean> changePassword(ChangePasswordRequest request, Authentication connectedUser) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        String username = keycloakAdminService.getUserNameById(connectedUser.getName());

        if (!keycloakAdminService.isOldPasswordValid(username, request.oldPassword())) {
            throw new CustomResponseException(ErrorCode.OLD_PASSWORD_INVALID);
        }
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new CustomResponseException(ErrorCode.CONFIRM_NEW_PASSWORD_INVALID);
        }
        keycloakAdminService.resetUserPassword(connectedUser.getName(), request.newPassword());
        response.setData(true);
        response.setMessage("Password is changed successful!");
        return response;
    }
}
