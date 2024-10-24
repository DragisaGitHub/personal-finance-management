package com.dragi.finance_manager.transaction;

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

    @Scheduled(cron = "0 0/1 * * * *")
    public void processRecurringTransactions() {
        List<RecurringTransaction> dueTransactions = recurringTransactionRepository.findByNextOccurrenceBefore(LocalDate.now());

        for (RecurringTransaction recurring : dueTransactions) {
            Transaction transaction = recurring.getTransaction();
            transaction.setDate(LocalDate.now());
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

    private LocalDate calculateNextOccurrence(RecurringTransaction recurringTransaction) {
        return switch (recurringTransaction.getFrequency()) {
            case "daily" -> recurringTransaction.getNextOccurrence().plusDays(1);
            case "weekly" -> recurringTransaction.getNextOccurrence().plusWeeks(1);
            case "monthly" -> recurringTransaction.getNextOccurrence().plusMonths(1);
            default -> throw new IllegalArgumentException("Invalid frequency");
        };
    }

    public void deleteRecurringTransaction(Long id) {
        recurringTransactionRepository.deleteById(id);
    }
}

