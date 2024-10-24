package com.dragi.finance_manager.reports.income_expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeExpenseReportRepository extends JpaRepository<IncomeExpenseReport, Long> {
    Optional<IncomeExpenseReport> findByYear(int year);
}
