package com.dragi.finance_manager.budget;

import com.dragi.finance_manager.utils.HelperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget createOrUpdateBudget(Budget budget) {
        budget.setUsername(HelperUtils.getAuthenticatedUsername());
        return budgetRepository.save(budget);
    }

    public List<Budget> getBudgetsForMonth(String username, int year, int month) {
        return budgetRepository.findByUsernameAndYearAndMonth(username, year, month);
    }
}
