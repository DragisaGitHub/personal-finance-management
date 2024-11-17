package com.dragi.finance_manager.cv;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SocialLinkResponse(
        String platform,
        String url,
        String iconClass,
        Long cd_id
) {}
