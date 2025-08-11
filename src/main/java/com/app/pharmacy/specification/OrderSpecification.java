package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.OrderLog;
import com.app.pharmacy.domain.entity.SaleLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<OrderLog> hasOrderDate(LocalDateTime orderDateBegin, LocalDateTime orderDateEnd) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (orderDateBegin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), orderDateBegin));
            }
            if (orderDateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), orderDateEnd));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
    }

    public static Specification<OrderLog> hasCustomerId(String customerId) {
        return ((root, query, criteriaBuilder) -> {
            if (customerId == null || customerId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("createdBy"), customerId);
        });
    }
}
