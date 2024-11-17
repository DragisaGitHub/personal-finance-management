package com.dragi.finance_manager.budget;

public class BudgetNotFoundException extends RuntimeException {

    public BudgetNotFoundException(Long id) {
        super("Could not find budget " + id);
    }
}