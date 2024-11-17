package com.dragi.finance_manager.budget;

import com.dragi.finance_manager.utils.HelperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Budgets", description = "API for managing user budgets by category and month.")
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetModelAssembler budgetModelAssembler;

    public BudgetController(BudgetService budgetService, BudgetModelAssembler budgetModelAssembler) {
        this.budgetService = budgetService;
        this.budgetModelAssembler = budgetModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Create or update a budget", description = "Create a new budget or update an existing one for a specific category and month.")
    public ResponseEntity<?> createOrUpdateBudget(@RequestBody Budget budget) {
        String username = HelperUtils.getAuthenticatedUsername();
        budget.setUsername(username);
        EntityModel<Budget> entityModel = budgetModelAssembler.toModel(budgetService.createOrUpdateBudget(budget));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping
    @Operation(summary = "Get budgets for a specific month", description = "Retrieve all budgets set by the user for a given year and month.")
    public CollectionModel<EntityModel<Budget>> getBudgetsForMonth(@RequestParam int year, @RequestParam int month) {
        String username = HelperUtils.getAuthenticatedUsername();
        List<EntityModel<Budget>> budgets = budgetService.getBudgetsForMonth(username, year, month).stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(budgets, linkTo(methodOn(BudgetController.class).getBudgetsForMonth(year, month)).withSelfRel());

    }
}