package com.dragi.finance_manager.savings;

public class SavingsGoalNotFoundException extends RuntimeException {
    public SavingsGoalNotFoundException(Long id) {
        super("Could not find savings goal with id " + id);
    }
}