package com.dragi.finance_manager.reports;

import org.springframework.stereotype.Service;

@Service
public class ReportsService {

    private final MonthlyReportRepository monthlyReportRepository;
    private final IncomeExpenseReportRepository incomeExpenseReportRepository;
    private final CategoryWiseReportRepository categoryWiseReportRepository;

    public ReportsService(MonthlyReportRepository monthlyReportRepository,
                          IncomeExpenseReportRepository incomeExpenseReportRepository,
                          CategoryWiseReportRepository categoryWiseReportRepository) {
        this.monthlyReportRepository = monthlyReportRepository;
        this.incomeExpenseReportRepository = incomeExpenseReportRepository;
        this.categoryWiseReportRepository = categoryWiseReportRepository;
    }

    public MonthlyReport getMonthlyReport(int year, int month) {
        return monthlyReportRepository.findByYearAndMonth(year, month)
                .orElseThrow(() -> new ReportNotFoundException(year, month));
    }

    public IncomeExpenseReport getIncomeExpenseReport(int year) {
        return incomeExpenseReportRepository.findByYear(year)
                .orElseThrow(() -> new ReportNotFoundException(year));
    }

    public CategoryWiseReport getCategoryWiseReport(int year, int month) {
        return categoryWiseReportRepository.findByYearAndMonth(year, month)
                .orElseThrow(() -> new ReportNotFoundException(year, month));
    }
}
