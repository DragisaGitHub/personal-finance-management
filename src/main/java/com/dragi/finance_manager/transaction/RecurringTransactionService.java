package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.enums.TransactionType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionService transactionService;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository, TransactionService transactionService) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.transactionService = transactionService;
    }

    @Scheduled(cron = "0 0 1 * * *") // Daily at 1 AM
    public void processRecurringTransactions() {
        List<RecurringTransaction> dueTransactions = recurringTransactionRepository.findByNextOccurrenceBefore(LocalDate.now());

        for (RecurringTransaction recurring : dueTransactions) {
            Transaction transaction = mapToTransaction(recurring);
            transactionService.createTransaction(transaction);
            recurring.setLastOccurrence(LocalDate.now());
            recurring.setNextOccurrence(calculateNextOccurrence(recurring));
            recurringTransactionRepository.save(recurring);
        }
    }

    public RecurringTransaction createRecurringTransaction(RecurringTransaction recurringTransaction) {
        recurringTransaction.setNextOccurrence(calculateNextOccurrence(recurringTransaction));
        return recurringTransactionRepository.save(recurringTransaction);
    }

    public Transaction mapToTransaction(RecurringTransaction recurringTransaction) {
        Transaction transaction = new Transaction();
        transaction.setAmount(recurringTransaction.getAmount().doubleValue());
        transaction.setDescription("Recurring: " + recurringTransaction.getCategory());
        transaction.setDate(LocalDate.now());
        transaction.setUsername("user-" + recurringTransaction.getUserId());
        transaction.setCategory(recurringTransaction.getCategory());
        transaction.setType(TransactionType.EXPENSE);

        return transaction;
    }

    private LocalDate calculateNextOccurrence(RecurringTransaction recurringTransaction) {
        return switch (recurringTransaction.getFrequency()) {
            case "daily" -> recurringTransaction.getNextOccurrence().plusDays(1);
            case "weekly" -> recurringTransaction.getNextOccurrence().plusWeeks(1);
            case "monthly" -> recurringTransaction.getNextOccurrence().plusMonths(1);
            default -> throw new IllegalArgumentException("Invalid frequency");
        };
    }
}

