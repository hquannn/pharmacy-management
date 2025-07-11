package com.app.pharmacy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Id
    @Column(name = "E_ID")
    private String id;
    @Column(name = "E_Username")
    private String username;
    @Column(name = "E_Fname")
    private String firstName;
    @Column(name = "E_Lname")
    private String lastName;
    @Column(name = "E_Bdate")
    private LocalDate birthDate;
    @Column(name = "E_Age")
    private Integer age;
    @Column(name = "E_Sex")
    private String sex;
    @Column(name = "E_Type")
    private String type;
    @Column(name = "E_Jdate")
    private LocalDateTime joinDate;
    @Column(name = "E_Add")
    private String address;
    @Column(name = "E_Mail")
    private String mail;
    @Column(name = "E_Phno")
    private String phoneNo;
    @Column(name = "E_Sal")
    private BigDecimal salary;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "updated_by")
    private String updatedBy;
}
