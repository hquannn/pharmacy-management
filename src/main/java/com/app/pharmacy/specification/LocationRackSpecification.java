package com.app.pharmacy.specification;

import com.app.pharmacy.domain.entity.LocationRack;
import org.springframework.data.jpa.domain.Specification;

public class LocationRackSpecification {
    public static Specification<LocationRack> hasPosition(String position) {
        return (root, query, criteriaBuilder) -> {
            if (position == null || position.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), "%" + position.toLowerCase() + "%");
        };
    }
}
