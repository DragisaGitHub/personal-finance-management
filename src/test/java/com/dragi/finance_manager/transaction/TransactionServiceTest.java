package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.enums.TransactionType;
import com.dragi.finance_manager.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        transaction.setCategory(category);  // Added category to transaction
    }

    @Test
    void shouldCreateTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals("Salary", createdTransaction.getDescription());
        assertEquals("Groceries", createdTransaction.getCategory().getName());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldGetTransactionsByUserId() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        when(transactionRepository.findByUserId(1L)).thenReturn(transactions);

        List<Transaction> foundTransactions = transactionService.getTransactionsByUserId(1L);

        assertEquals(1, foundTransactions.size());
        verify(transactionRepository, times(1)).findByUserId(1L);
    }
}