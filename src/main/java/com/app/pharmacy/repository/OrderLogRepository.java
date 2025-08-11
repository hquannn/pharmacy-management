package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderLogRepository extends JpaRepository<OrderLog, String>, JpaSpecificationExecutor<OrderLog> {
}
