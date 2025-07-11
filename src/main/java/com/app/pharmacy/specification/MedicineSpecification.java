package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.Medicine;
import org.springframework.data.jpa.domain.Specification;

public class MedicineSpecification {
    public static Specification<Medicine> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Medicine> hasCategory(String categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null || categoryId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("categoryId"), categoryId);
        };
    }

    public static Specification<Medicine> hasQuantity(Integer quantity) {
        return (root, query, criteriaBuilder) -> {
            if (quantity == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("quantity"), quantity);
        };
    }
}
