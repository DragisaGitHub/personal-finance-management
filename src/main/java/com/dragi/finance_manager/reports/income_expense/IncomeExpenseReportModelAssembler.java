package com.dragi.finance_manager.reports.income_expense;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IncomeExpenseReportModelAssembler implements RepresentationModelAssembler<IncomeExpenseReport, EntityModel<IncomeExpenseReport>> {

    @Override
    public EntityModel<IncomeExpenseReport> toModel(IncomeExpenseReport report) {
        return EntityModel.of(report,
                linkTo(methodOn(IncomeExpenseReportController.class).getIncomeVsExpenseReport(report.getYear())).withSelfRel());
    }
}
