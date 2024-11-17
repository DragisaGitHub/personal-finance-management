package com.dragi.finance_manager.budget;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BudgetModelAssembler implements RepresentationModelAssembler<Budget, EntityModel<Budget>> {

    @Override
    public EntityModel<Budget> toModel(Budget budget) {
        return EntityModel.of(budget,
                linkTo(methodOn(BudgetController.class).getBudgetsForMonth(budget.getYear(), budget.getMonth())).withSelfRel(),
                linkTo(methodOn(BudgetController.class).createOrUpdateBudget(budget)).withRel("createOrUpdateBudget"));
    }
}