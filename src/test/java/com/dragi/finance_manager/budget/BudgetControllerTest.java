package com.dragi.finance_manager.budget;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.utils.HelperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private MockMvc mockMvc;
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
        budget.setCategory(category);  // Assign the Category instance
        budget.setAmount(500.00);
        budget.setYear(2023);
        budget.setMonth(10);
    }

    @Test
    void shouldCreateOrUpdateBudget() throws Exception {
        when(budgetService.createOrUpdateBudget(any(Budget.class))).thenReturn(budget);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"category\": \"Groceries\", \"amount\": 500.00, \"year\": 2023, \"month\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Groceries"))
                .andExpect(jsonPath("$.amount").value(500.00));

        verify(budgetService, times(1)).createOrUpdateBudget(any(Budget.class));
    }

    @Test
    void shouldGetBudgetsForMonth() throws Exception {
        when(budgetService.getBudgetsForMonth(username, 2023, 10)).thenReturn(Collections.singletonList(budget));

        mockMvc.perform(get("/api/budgets")
                        .param("year", "2023")
                        .param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Groceries"))
                .andExpect(jsonPath("$[0].amount").value(500.00));

        verify(budgetService, times(1)).getBudgetsForMonth(username, 2023, 10);
    }
}