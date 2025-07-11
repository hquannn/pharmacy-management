package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseRepository extends JpaRepository<Purchase, String>, JpaSpecificationExecutor<Purchase> {
    boolean existsByMedicineId(String medicineId);
    boolean existsBySupplierId(String supplierId);
}
