package com.dragi.finance_manager.cv;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record CVRequest(
        Long id,
        String username,
        String fullName,
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,
        String phoneNumber,
        String summary,
        String address,
        List<ExperienceRequest> experiences,
        List<EducationRequest> educations,
        List<SocialLinkRequest> socialLinks,
        List<String> skills,
        List<String> languages
) {}
