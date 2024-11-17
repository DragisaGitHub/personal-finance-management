package com.dragi.finance_manager.cv;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CVModelAssembler implements RepresentationModelAssembler<CVResponse, EntityModel<CVResponse>> {

    @Override
    public EntityModel<CVResponse> toModel(CVResponse cvResponse) {
        try {
            return EntityModel.of(cvResponse,
                    linkTo(methodOn(CVController.class).getCVById(cvResponse.id())).withSelfRel(),
                    linkTo(methodOn(CVController.class).getAllCVs()).withRel("allCVs"),
                    linkTo(methodOn(CVController.class).updateCV(null, cvResponse.id(), null)).withRel("updateCV"),
                    linkTo(methodOn(CVController.class).deleteCV(cvResponse.id(), cvResponse.fullName())).withRel("deleteCV")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}