package com.dragi.finance_manager.reports.category_wise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryWiseReportRepository extends JpaRepository<CategoryWiseReport, Long> {
    Optional<CategoryWiseReport> findByYearAndMonth(int year, int month);
}

