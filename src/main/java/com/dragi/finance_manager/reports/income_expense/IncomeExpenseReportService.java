package com.dragi.finance_manager.reports.income_expense;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class IncomeExpenseReportService {

    private final IncomeExpenseReportRepository incomeExpenseReportRepository;

    public IncomeExpenseReportService(IncomeExpenseReportRepository incomeExpenseReportRepository) {
        this.incomeExpenseReportRepository = incomeExpenseReportRepository;
    }

    public Optional<IncomeExpenseReport> getIncomeExpenseReport(int year) {
        return incomeExpenseReportRepository.findByYear(year);
    }

    public void deleteReport(Long id) {
        incomeExpenseReportRepository.delete(Objects.requireNonNull(incomeExpenseReportRepository.findById(id).orElse(null)));
    }

    public IncomeExpenseReport createReport(IncomeExpenseReport report) {
        return incomeExpenseReportRepository.save(report);
    }
}
