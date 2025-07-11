package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.MedicineUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineUnitRepository extends JpaRepository<MedicineUnit, String> {

}
