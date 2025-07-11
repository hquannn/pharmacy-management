package com.app.pharmacy.repository;

import com.app.pharmacy.domain.entity.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, String>, JpaSpecificationExecutor<MedicineCategory>{
}
