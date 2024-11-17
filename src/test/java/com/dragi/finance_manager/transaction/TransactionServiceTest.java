package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        Category category = new Category();
        category.setId(1L);
        category.setName("Groceries");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Salary");
        transaction.setAmount(5000);
        transaction.setDate(LocalDate.now());
        transaction.setType(TransactionType.INCOME);
        transaction.setCategory(category);
    }

    @Test
    void shouldCreateTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals("Salary", createdTransaction.getDescription());
        assertEquals(5000, createdTransaction.getAmount());
        assertEquals("Groceries", createdTransaction.getCategory().getName());
        assertEquals(TransactionType.INCOME, createdTransaction.getType());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldGetTransactionsByUserName() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("dragisa");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        when(transactionRepository.getTransactionsByUsername("dragisa")).thenReturn(transactions);

        List<Transaction> foundTransactions = transactionService.getTransactionsByUsername("dragisa");

        assertEquals(1, foundTransactions.size());
        verify(transactionRepository, times(1)).getTransactionsByUsername("dragisa");
    }

}