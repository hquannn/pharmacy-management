package com.app.pharmacy.domain.dto.customer;

import com.app.pharmacy.domain.dto.employee.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private EmployeeRole role;
    private Integer age;
    private String sex;
    private String phoneNo;
    private String mail;
    private BigDecimal points;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
}
