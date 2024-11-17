package com.dragi.finance_manager.cv;

import com.dragi.finance_manager.utils.PDFUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CVService {

    private final CVRepository cvRepository;
    private final CVMapper cvMapper;

    public CVService(CVRepository cvRepository, CVMapper cvMapper) {
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }

    public CVResponse createCV(CVRequest cvRequest, String username, MultipartFile imageFile) throws IOException {
        CV cv = cvMapper.toEntity(cvRequest);
        cv.setUsername(username);

        if (imageFile != null && !imageFile.isEmpty()) {
            cv.setProfileImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        }

        return cvMapper.toResponse(cvRepository.save(cv));
    }

    public Optional<CVResponse> getCVById(Long id) {
        return cvRepository.findById(id).map(cvMapper::toResponse);
    }

    public List<CVResponse> getAllCVs() {
        return cvRepository.findAll().stream()
                .map(cvMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CVResponse updateOrCreateCV(CVRequest cvRequest, Long id, String username, MultipartFile imageFile) {
        CV cv = cvRepository.findById(id)
                .map(existingCV -> {
                    CV updatedCV = cvMapper.updateEntityFromRequest(existingCV, cvRequest);
                    updatedCV.setUsername(username);

                    // Handle profile image
                    if (imageFile != null && !imageFile.isEmpty()) {
                        try {
                            updatedCV.setProfileImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        updatedCV.setProfileImage(existingCV.getProfileImage());
                    }

                    // Update educations
                    updateEducationCollection(existingCV.getEducations(), cvRequest.educations(), updatedCV);

                    // Update experiences
                    updateExperienceCollection(existingCV.getExperiences(), cvRequest.experiences(), updatedCV);

                    // Update socialLinks
                    updateSocialLinkCollection(existingCV.getSocialLinks(), cvRequest.socialLinks(), updatedCV);

                    return cvRepository.save(existingCV); // Use existingCV to persist changes
                })
                .orElseGet(() -> {
                    CV newCV = cvMapper.toEntity(cvRequest);
                    newCV.setUsername(username);

                    if (imageFile != null && !imageFile.isEmpty()) {
                        try {
                            newCV.setProfileImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return cvRepository.save(newCV);
                });

        return cvMapper.toResponse(cv);
    }

    private void updateSocialLinkCollection(
            List<SocialLink> existingSocialLinks, List<SocialLinkRequest> newSocialLinks, CV updatedCV) {
        // Remove social links not in the new list
        existingSocialLinks.removeIf(socialLink ->
                newSocialLinks.stream().noneMatch(req -> req.cd_id() != null && req.cd_id().equals(socialLink.getId())));

        // Update or add social links
        for (SocialLinkRequest req : newSocialLinks) {
            SocialLink socialLink = existingSocialLinks.stream()
                    .filter(s -> s.getId() != null && s.getId().equals(req.cd_id()))
                    .findFirst()
                    .orElseGet(() -> {
                        SocialLink newSocialLink = new SocialLink();
                        newSocialLink.setCv(updatedCV); // Set parent CV
                        existingSocialLinks.add(newSocialLink);
                        return newSocialLink;
                    });

            socialLink.setPlatform(req.platform());
            socialLink.setUrl(req.url());
            socialLink.setIconClass(req.iconClass());
        }
    }

    private void updateExperienceCollection(List<Experience> existingExperiences, List<ExperienceRequest> newExperiences, CV updatedCV) {
        existingExperiences.removeIf(experience ->
                newExperiences.stream().noneMatch(req -> req.cd_id() != null && req.cd_id().equals(experience.getId())));

        for (ExperienceRequest req : newExperiences) {
            Experience experience = existingExperiences.stream()
                    .filter(e -> e.getId() != null && e.getId().equals(req.cd_id()))
                    .findFirst()
                    .orElseGet(() -> {
                        Experience newExperience = new Experience();
                        newExperience.setCv(updatedCV); // Set parent CV
                        existingExperiences.add(newExperience);
                        return newExperience;
                    });

            experience.setDescription(req.description());
            experience.setCompany(req.company());
            experience.setPosition(req.position());
            experience.setStartDate(req.startDate());
            experience.setEndDate(req.endDate());
        }
    }

    private void updateEducationCollection(
            List<Education> existingEducations, List<EducationRequest> newEducations, CV cv) {
        // Remove educations not in the new list
        existingEducations.removeIf(education ->
                newEducations.stream().noneMatch(req -> req.cd_id() != null && req.cd_id().equals(education.getId())));

        // Update or add educations
        for (EducationRequest req : newEducations) {
            Education education = existingEducations.stream()
                    .filter(e -> e.getId() != null && e.getId().equals(req.cd_id()))
                    .findFirst()
                    .orElseGet(() -> {
                        Education newEducation = new Education();
                        newEducation.setCv(cv); // Set parent CV
                        existingEducations.add(newEducation);
                        return newEducation;
                    });

            education.setDegree(req.degree());
            education.setInstitution(req.institution());
            education.setStartDate(req.startDate());
            education.setEndDate(req.endDate());
        }
    }

// Similar methods for experiences and socialLinks...


    public void deleteCV(Long id) {
        cvRepository.deleteById(id);
    }

    public void deleteByFullName(String fullName) {
        if (cvRepository.findByFullName(fullName).isPresent()) {
            cvRepository.deleteByFullName(fullName);
        } else {
            throw new RuntimeException("CV with full name " + fullName + " not found");
        }
    }

    public byte[] generatePDF(Long cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV with ID " + cvId + " not found"));

        return PDFUtils.createCVPDF(cv);
    }
}