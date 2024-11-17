package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.utils.HelperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "API for managing financial transactions.")
public class TransactionController {

    private final TransactionService transactionService;
    private final RecurringTransactionService recurringTransactionService;
    private final TransactionModelAssembler transactionModelAssembler;

    public TransactionController(TransactionService transactionService, RecurringTransactionService recurringTransactionService, TransactionModelAssembler transactionModelAssembler) {
        this.transactionService = transactionService;
        this.recurringTransactionService = recurringTransactionService;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    @GetMapping("/user")
    @Operation(summary = "Get user's transactions", description = "Retrieve all transactions for the currently authenticated user.")
    public List<EntityModel<Transaction>> getTransactionsForCurrentUser() {
        String username = HelperUtils.getAuthenticatedUsername();
        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);

        return transactions.stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by its unique ID.")
    public EntityModel<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionModelAssembler.toModel(transaction);
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Create a new financial transaction for the authenticated user.")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        String username = HelperUtils.getAuthenticatedUsername();
        transaction.setUsername(username);
        transaction.setDate(LocalDate.now());
        EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(transactionService.createTransaction(transaction));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PostMapping("/recurring")
    @Operation(summary = "Create a new recurring transaction", description = "Create a new recurring transaction for the authenticated user.")
    public ResponseEntity<?> createRecurringTransaction(@RequestBody RecurringTransaction recurringTransaction) {
        RecurringTransaction savedRecurringTransaction = recurringTransactionService.createRecurringTransaction(recurringTransaction);
        EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(savedRecurringTransaction.getTransaction());
        System.out.println("entityModel: " + entityModel);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update transaction", description = "Update financial transaction for the authenticated user.")
    ResponseEntity<?> replaceTransaction(@RequestBody Transaction newTransaction, @PathVariable Long id) {

        Transaction updatedTransaction = transactionService.getTransactionById(id)
                .map(transaction -> {
                    transaction.setDescription(newTransaction.getDescription());
                    transaction.setAmount(newTransaction.getAmount());
                    transaction.setDate(newTransaction.getDate());
                    transaction.setType(newTransaction.getType());
                    transaction.setCategory(newTransaction.getCategory());
                    return transactionService.createTransaction(transaction);
                })
                .orElseGet(() -> {
                    newTransaction.setId(id);
                    return transactionService.createTransaction(newTransaction);
                });

        EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(updatedTransaction);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/all")
    @Operation(summary = "Show list of all transactions", description = "Show all financial transactions for the authenticated user.")
    public List<EntityModel<Transaction>> getAllTransactions() {
        return transactionService.getAllTransactions().stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete financial transaction for the authenticated user.")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        recurringTransactionService.deleteRecurringTransaction(id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
