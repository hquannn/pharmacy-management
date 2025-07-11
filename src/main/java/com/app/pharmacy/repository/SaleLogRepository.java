package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.SaleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaleLogRepository  extends JpaRepository<SaleLog, String>, JpaSpecificationExecutor<SaleLog> {

}
