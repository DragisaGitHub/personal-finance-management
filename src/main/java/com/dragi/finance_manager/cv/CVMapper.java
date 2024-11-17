package com.dragi.finance_manager.cv;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CVMapper {

    CVMapper INSTANCE = Mappers.getMapper(CVMapper.class);

    CVResponse toResponse(CV cv);
    CV toEntity(CVRequest cvRequest);
    @Mapping(target = "id", ignore = true)
    CV updateEntityFromRequest(@MappingTarget CV cv, CVRequest cvRequest);


    // Map Education to EducationResponse
    @Mapping(source = "cv.id", target = "cd_id")
    EducationResponse toEducationResponse(Education education);

    // Map EducationRequest to Education
    @Mapping(source = "cd_id", target = "cv.id") // Map cd_id back to cv.id
    @Mapping(target = "cv", ignore = true) // Avoid overwriting the cv reference directly
    Education toEducationEntity(EducationRequest educationRequest);

    // Map Experience to ExperienceResponse (if needed)
    ExperienceResponse toExperienceResponse(Experience experience);

    // Map ExperienceRequest to Experience
    Experience toExperienceEntity(ExperienceRequest experienceRequest);
}