package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {
    public static Specification<Supplier> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
