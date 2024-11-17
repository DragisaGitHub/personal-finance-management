package com.dragi.finance_manager.cv;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EducationRequest(
        String degree,
        String institution,
        LocalDate startDate,
        LocalDate endDate,
        Long cd_id
) {}
