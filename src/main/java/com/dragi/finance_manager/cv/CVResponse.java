package com.dragi.finance_manager.cv;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record CVResponse(
        Long id,
        String username,
        String fullName,
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,
        String phoneNumber,
        String summary,
        String address,
        List<ExperienceResponse> experiences,
        List<EducationResponse> educations,
        List<SocialLinkResponse> socialLinks,
        List<String> skills,
        List<String> languages,
        String profileImage
) {}