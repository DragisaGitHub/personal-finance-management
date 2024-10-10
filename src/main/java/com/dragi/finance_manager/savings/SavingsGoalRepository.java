package com.dragi.finance_manager.savings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findByUsername(String username);
    Optional<SavingsGoal> findByIdAndUsername(Long id, String username);
}