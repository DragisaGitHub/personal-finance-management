package com.dragi.finance_manager.reports.monthly;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MonthlyReportModelAssembler implements RepresentationModelAssembler<MonthlyReport, EntityModel<MonthlyReport>> {

    @Override
    public EntityModel<MonthlyReport> toModel(MonthlyReport report) {
        return EntityModel.of(report,
                linkTo(methodOn(MonthlyReportController.class).getMonthlyReport(report.getYear(), report.getMonth())).withSelfRel());
    }
}
