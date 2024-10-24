package com.dragi.finance_manager.reports.monthly;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dragi.finance_manager.reports.ReportNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports/monthly")
@Tag(name = "Monthly Reports", description = "API for creating and managing monthly financial reports.")
public class MonthlyReportController {

    private final MonthlyReportService monthlyReportService;
    private final MonthlyReportModelAssembler monthlyReportModelAssembler;

    public MonthlyReportController(MonthlyReportService monthlyReportService, MonthlyReportModelAssembler monthlyReportModelAssembler) {
        this.monthlyReportService = monthlyReportService;
        this.monthlyReportModelAssembler = monthlyReportModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Create a monthly report", description = "Generate a monthly financial report for a specific period.")
    public ResponseEntity<?> createReport(@RequestBody MonthlyReport report) {
        EntityModel<MonthlyReport> entityModel = monthlyReportModelAssembler.toModel(monthlyReportService.createReport(report));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a monthly report", description = "Delete a monthly report by its ID.")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        monthlyReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get monthly report", description = "Retrieve a monthly report for a specific year and month.")
    public EntityModel<MonthlyReport> getMonthlyReport(@RequestParam int year, @RequestParam int month) {
        MonthlyReport monthlyReport = monthlyReportService.getMonthlyReport(year, month).orElseThrow(() -> new ReportNotFoundException(year, month));
        return monthlyReportModelAssembler.toModel(monthlyReport);
    }
}