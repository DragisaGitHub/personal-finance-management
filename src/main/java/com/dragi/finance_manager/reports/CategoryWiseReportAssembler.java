package com.dragi.finance_manager.reports;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryWiseReportAssembler implements RepresentationModelAssembler<CategoryWiseReport, EntityModel<CategoryWiseReport>> {

    @Override
    public EntityModel<CategoryWiseReport> toModel(CategoryWiseReport report) {
        return EntityModel.of(report,
                linkTo(methodOn(ReportsController.class).getCategoryWiseReport(report.getYear(), report.getMonth())).withSelfRel());
    }
}
