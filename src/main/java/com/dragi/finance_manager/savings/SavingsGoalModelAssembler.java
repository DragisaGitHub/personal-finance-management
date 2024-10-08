package com.dragi.finance_manager.savings;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SavingsGoalModelAssembler implements RepresentationModelAssembler<SavingsGoal, EntityModel<SavingsGoal>> {

    @Override
    public EntityModel<SavingsGoal> toModel(SavingsGoal savingsGoal) {
        return EntityModel.of(savingsGoal,
                linkTo(methodOn(SavingsGoalController.class).getSavingsGoalById(savingsGoal.getId())).withSelfRel(),
                linkTo(methodOn(SavingsGoalController.class).getUserSavingsGoals()).withRel("savings-goals"));
    }
}