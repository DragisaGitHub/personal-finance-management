package com.dragi.finance_manager.reports;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/monthly")
    public MonthlyReport getMonthlyReport(@RequestParam int year, @RequestParam int month) {
        return reportsService.getMonthlyReport(year, month);
    }

    @GetMapping("/income-expense")
    public IncomeExpenseReport getIncomeVsExpenseReport(@RequestParam int year) {
        return reportsService.getIncomeExpenseReport(year);
    }

    @GetMapping("/category-wise")
    public CategoryWiseReport getCategoryWiseReport(@RequestParam int year, @RequestParam int month) {
        return reportsService.getCategoryWiseReport(year, month);
    }
}

