package com.dragi.finance_manager.reports.category_wise;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryWiseReportService {

    private final CategoryWiseReportRepository categoryWiseReportRepository;

    public CategoryWiseReportService(CategoryWiseReportRepository categoryWiseReportRepository) {
        this.categoryWiseReportRepository = categoryWiseReportRepository;
    }

    public Optional<CategoryWiseReport> getCategoryWiseReport(int year, int month) {
        return categoryWiseReportRepository.findByYearAndMonth(year, month);
    }

    public CategoryWiseReport createReport(CategoryWiseReport report) {
        return categoryWiseReportRepository.save(report);
    }

    public void deleteReport(Long id) {
        categoryWiseReportRepository.delete(Objects.requireNonNull(categoryWiseReportRepository.findById(id).orElse(null)));
    }
}
