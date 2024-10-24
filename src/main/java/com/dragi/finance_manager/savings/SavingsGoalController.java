package com.dragi.finance_manager.savings;

import com.dragi.finance_manager.util.HelperUtils;
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
@RequestMapping("/api/savings-goals")
@Tag(name = "Savings Goals", description = "The Savings Goals API")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;
    private final SavingsGoalModelAssembler savingsGoalModelAssembler;

    public SavingsGoalController(SavingsGoalService savingsGoalService, SavingsGoalModelAssembler savingsGoalModelAssembler) {
        this.savingsGoalService = savingsGoalService;
        this.savingsGoalModelAssembler = savingsGoalModelAssembler;
    }

    // Create or update a savings goal
    @PostMapping
    @Operation(summary = "Create or Update a Savings Goal")
    public ResponseEntity<?> createOrUpdateGoal(@RequestBody SavingsGoal savingsGoal) {
        String username = HelperUtils.getAuthenticatedUsername();
        savingsGoal.setUsername(username);

        SavingsGoal savedGoal = savingsGoalService.saveOrUpdateGoal(savingsGoal);
        EntityModel<SavingsGoal> entityModel = savingsGoalModelAssembler.toModel(savedGoal);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}/add")
    public ResponseEntity<?> addToGoal(@PathVariable Long id, @RequestParam double amount) {
        String username = HelperUtils.getAuthenticatedUsername();
        SavingsGoal updatedGoal = savingsGoalService.addToGoal(id, amount, username);
        EntityModel<SavingsGoal> entityModel = savingsGoalModelAssembler.toModel(updatedGoal);

        return ResponseEntity.ok(entityModel);
    }

    // Get all savings goals for the current user
    @GetMapping
    public CollectionModel<EntityModel<SavingsGoal>> getUserSavingsGoals() {
        String username = HelperUtils.getAuthenticatedUsername();
        List<EntityModel<SavingsGoal>> goals = savingsGoalService.getUserSavingsGoals(username).stream()
                .map(savingsGoalModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(goals, linkTo(methodOn(SavingsGoalController.class).getUserSavingsGoals()).withSelfRel());
    }

    // Get a specific savings goal by ID
    @GetMapping("/{id}")
    public EntityModel<SavingsGoal> getSavingsGoalById(@PathVariable Long id) {
        SavingsGoal savingsGoal = savingsGoalService.getSavingsGoalById(id)
                .orElseThrow(() -> new SavingsGoalNotFoundException(id));
        return savingsGoalModelAssembler.toModel(savingsGoal);
    }

    // Delete a savings goal by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSavingsGoal(@PathVariable Long id) {
        savingsGoalService.deleteSavingsGoal(id);
        return ResponseEntity.noContent().build();
    }
}