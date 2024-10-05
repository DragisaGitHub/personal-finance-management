package com.dragi.finance_manager.transaction;

import org.keycloak.KeycloakPrincipal;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionModelAssembler transactionModelAssembler;

    public TransactionController(TransactionService transactionService, TransactionModelAssembler transactionModelAssembler) {
        this.transactionService = transactionService;
        this.transactionModelAssembler = transactionModelAssembler;
    }
    
    @GetMapping("/user")
    public List<EntityModel<Transaction>> getTransactionsForCurrentUser() {
        String username = getAuthenticatedUsername();
        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);

        return transactions.stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EntityModel<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionModelAssembler.toModel(transaction);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        String username = getAuthenticatedUsername();
        transaction.setUsername(username);
        EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(transactionService.createTransaction(transaction));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/transactions/{id}")
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
    public CollectionModel<EntityModel<Transaction>> getAllTransactions() {

        List<EntityModel<Transaction>> employees = transactionService.getAllTransactions().stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(TransactionController.class).getAllTransactions()).withSelfRel());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    private String getAuthenticatedUsername() {
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }
}