package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.enums.TransactionType;
import com.dragi.finance_manager.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        Category category = new Category();
        category.setId(1L);
        category.setName("Groceries");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Salary");
        transaction.setAmount(5000);
        transaction.setDate(LocalDate.now());
        transaction.setType(TransactionType.INCOME);
        transaction.setUser(user);
        transaction.setCategory(category);
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Salary\", \"amount\": 5000, \"type\": \"INCOME\", \"user\": {\"id\": 1}, \"category\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.category.name").value("Groceries"));

        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void shouldGetTransactionsByUser() throws Exception {
        when(transactionService.getTransactionsByUserId(1L)).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/api/transactions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Salary"))
                .andExpect(jsonPath("$[0].category.name").value("Groceries"));

        verify(transactionService, times(1)).getTransactionsByUserId(1L);
    }
}