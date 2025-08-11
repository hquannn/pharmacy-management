package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.Cart;
import com.app.pharmacy.domain.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByMedicineIdAndMfgDate(String medicineId, LocalDate mfgDate);
    List<Cart> findByIdIn(List<String> ids);
    boolean existsByLocationRackId(String locationRackId);
}
