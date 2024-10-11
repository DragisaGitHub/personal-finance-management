package com.dragi.finance_manager.reports;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(int year, int month) {
        super("Monthly Report not found for year: " + year + ", month: " + month);
    }

    public ReportNotFoundException(int year) {
        super("Monthly Report not found for year: " + year);
    }
}
