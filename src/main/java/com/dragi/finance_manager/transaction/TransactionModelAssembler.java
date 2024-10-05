package com.dragi.finance_manager.transaction;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionModelAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

    @Override
    public EntityModel<Transaction> toModel(Transaction employee) {

        return EntityModel.of(employee, //
                linkTo(methodOn(TransactionController.class).getTransactionById(employee.getId())).withSelfRel(),
                linkTo(methodOn(TransactionController.class).getAllTransactions()).withRel("transactions"));
    }
}
