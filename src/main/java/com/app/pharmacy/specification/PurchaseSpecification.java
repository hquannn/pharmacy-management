package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.Purchase;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSpecification {
    public static Specification<Purchase> hasSupplierName(String supplierName) {
        return (root, query, criteriaBuilder) -> {
            if (supplierName == null || supplierName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> supplier = root.join("supplier", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(supplier.get("name")), "%" + supplierName.toLowerCase() + "%");
        };
    }

    public static Specification<Purchase> hasMedicineName(String medicineName) {
        return (root, query, criteriaBuilder) -> {
            if (medicineName == null || medicineName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> medicine = root.join("medicine", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(medicine.get("name")), "%" + medicineName.toLowerCase() + "%");
        };
    }

    public static Specification<Purchase> hasPurchaseDate(LocalDateTime purchaseDateBegin, LocalDateTime purchaseDateEnd) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (purchaseDateBegin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), purchaseDateBegin));
            }
            if (purchaseDateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), purchaseDateEnd));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
    }
}
