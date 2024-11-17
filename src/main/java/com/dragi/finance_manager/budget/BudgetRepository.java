package com.dragi.finance_manager.budget;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUsernameAndYearAndMonth(String username, int year, int month);
}
