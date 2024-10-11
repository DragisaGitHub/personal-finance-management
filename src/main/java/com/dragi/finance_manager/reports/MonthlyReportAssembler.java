package com.dragi.finance_manager.reports;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MonthlyReportAssembler implements RepresentationModelAssembler<MonthlyReport, EntityModel<MonthlyReport>> {

    @Override
    public EntityModel<MonthlyReport> toModel(MonthlyReport report) {
        return EntityModel.of(report,
                linkTo(methodOn(ReportsController.class).getMonthlyReport(report.getYear(), report.getMonth())).withSelfRel());
    }
}
