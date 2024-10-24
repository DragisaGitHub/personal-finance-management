package com.dragi.finance_manager.reports.category_wise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dragi.finance_manager.reports.ReportNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports/category-wise")
@Tag(name = "Category-Wise Reports", description = "API for generating and managing category-wise financial reports.")
public class CategoryWiseReportController {

    private final CategoryWiseReportService categoryWiseReportService;
    private final CategoryWiseReportModelAssembler categoryWiseReportModelAssembler;

    public CategoryWiseReportController(CategoryWiseReportService categoryWiseReportService, CategoryWiseReportModelAssembler categoryWiseReportModelAssembler) {
        this.categoryWiseReportService = categoryWiseReportService;
        this.categoryWiseReportModelAssembler = categoryWiseReportModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Create a category-wise report", description = "Generate a new report based on category-wise financial data.")
    public ResponseEntity<?> createReport(@RequestBody CategoryWiseReport report) {
        EntityModel<CategoryWiseReport> entityModel = categoryWiseReportModelAssembler.toModel(categoryWiseReportService.createReport(report));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category-wise report", description = "Delete a category-wise report by its ID.")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        categoryWiseReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get category-wise report", description = "Retrieve a category-wise report for a specific year and month.")
    public EntityModel<CategoryWiseReport> getCategoryWiseReport(@RequestParam int year, @RequestParam int month) {
        CategoryWiseReport categoryWiseReport = categoryWiseReportService.getCategoryWiseReport(year, month).orElseThrow(() -> new ReportNotFoundException(year, month));
        return categoryWiseReportModelAssembler.toModel(categoryWiseReport);
    }
}
