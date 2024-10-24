package com.dragi.finance_manager.reports.income_expense;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dragi.finance_manager.reports.ReportNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports/income-expense")
@Tag(name = "Income vs Expense Reports", description = "API for managing income vs. expense financial reports.")
public class IncomeExpenseReportController {

    private final IncomeExpenseReportService incomeExpenseReportService;
    private final IncomeExpenseReportModelAssembler incomeExpenseReportModelAssembler;

    public IncomeExpenseReportController(IncomeExpenseReportService incomeExpenseReportService, IncomeExpenseReportModelAssembler incomeExpenseReportModelAssembler) {
        this.incomeExpenseReportService = incomeExpenseReportService;
        this.incomeExpenseReportModelAssembler = incomeExpenseReportModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Create an income vs expense report", description = "Generate a report comparing income and expenses for a period.")
    public ResponseEntity<?> createReport(@RequestBody IncomeExpenseReport report) {
        EntityModel<IncomeExpenseReport> entityModel = incomeExpenseReportModelAssembler.toModel(incomeExpenseReportService.createReport(report));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an income vs expense report", description = "Delete an income vs. expense report by its ID.")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        incomeExpenseReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get income vs expense report", description = "Retrieve an income vs. expense report for a specific year.")
    public EntityModel<IncomeExpenseReport> getIncomeVsExpenseReport(@RequestParam int year) {
        IncomeExpenseReport incomeExpenseReport = incomeExpenseReportService.getIncomeExpenseReport(year).orElseThrow(() -> new ReportNotFoundException(year));
        return incomeExpenseReportModelAssembler.toModel(incomeExpenseReport);
    }
}
