package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    boolean existsByCustomerId(String customerId);
}
