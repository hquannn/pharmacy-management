package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.Inventory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {
    public static Specification<Inventory> hasMedicineName(String medicineName) {
        return (root, query, criteriaBuilder) -> {
            if (medicineName == null || medicineName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> medicine = root.join("medicine", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(medicine.get("name")), "%" + medicineName.toLowerCase() + "%");
        };
    }

    public static Specification<Inventory> hasQuantity(Integer quantity) {
        return (root, query, criteriaBuilder) -> {
            if (quantity == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("quantity"), quantity);
        };
    }

    public static Specification<Inventory> hasExpireDate(LocalDate expireDateBegin, LocalDate expireDateEnd) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (expireDateBegin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expDate"), expireDateBegin));
            }
            if (expireDateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expDate"), expireDateEnd));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
    }
}
