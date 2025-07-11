package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {
    public static Specification<Customer> hasFirstName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Customer> hasLastName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Customer> hasPhoneNo(String phoneNo) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNo == null || phoneNo.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal((root.get("phoneNo")), phoneNo);
        };
    }
}
