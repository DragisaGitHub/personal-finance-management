package com.dragi.finance_manager.budget;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.utils.HelperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    private Budget budget;
    String username = HelperUtils.getAuthenticatedUsername();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category();
        category.setId(1L);
        category.setName("Groceries");

        budget = new Budget();
        budget.setId(1L);
        budget.setCategory(category);
        budget.setAmount(500.00);
        budget.setYear(2023);
        budget.setMonth(10);
        budget.setUsername(username);
    }

    @Test
    void shouldCreateOrUpdateBudget() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget createdBudget = budgetService.createOrUpdateBudget(budget);

        assertNotNull(createdBudget);
        assertEquals("Groceries", createdBudget.getCategory().getName());
        assertEquals(500.00, createdBudget.getAmount());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void shouldGetBudgetsForMonth() {
        when(budgetRepository.findByUsernameAndYearAndMonth(username, 2023, 10))
                .thenReturn(Collections.singletonList(budget));

        List<Budget> foundBudgets = budgetService.getBudgetsForMonth(username, 2023, 10);

        assertEquals(1, foundBudgets.size());
        assertEquals("Groceries", foundBudgets.get(0).getCategory().getName());
        verify(budgetRepository, times(1)).findByUsernameAndYearAndMonth(username, 2023, 10);
    }
}