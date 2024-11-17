package com.dragi.finance_manager.cv;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExperienceRequest(
        String position,
        String company,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        Long cd_id
) {}