package com.dragi.finance_manager.reports.monthly;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MonthlyReportService {

    private final MonthlyReportRepository monthlyReportRepository;

    public MonthlyReportService(MonthlyReportRepository monthlyReportRepository) {
        this.monthlyReportRepository = monthlyReportRepository;
    }

    public Optional<MonthlyReport> getMonthlyReport(int year, int month) {
        return monthlyReportRepository.findByYearAndMonth(year, month);
    }

    public void deleteReport(Long id) {
        monthlyReportRepository.delete(Objects.requireNonNull(monthlyReportRepository.findById(id).orElse(null)));
    }

    public MonthlyReport createReport(MonthlyReport report) {
        return monthlyReportRepository.save(report);
    }
}

