package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.SaleLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleSpecification {
    public static Specification<SaleLog> hasSaleDate(LocalDateTime saleDateBegin, LocalDateTime saleDateEnd) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (saleDateBegin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), saleDateBegin));
            }
            if (saleDateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), saleDateEnd));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
    }

    public static Specification<SaleLog> hasEmployeeId(String employeeId) {
        return ((root, query, criteriaBuilder) -> {
            if (employeeId == null || employeeId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("createdBy"), employeeId);
        });
    }
}
