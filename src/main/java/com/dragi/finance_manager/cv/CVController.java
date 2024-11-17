package com.dragi.finance_manager.cv;

import com.dragi.finance_manager.utils.HelperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/cvs")
@Tag(name = "CVs", description = "API for managing users' CVs.")
public class CVController {

    private final CVService cvService;
    private final CVModelAssembler cvModelAssembler;

    public CVController(CVService cvService, CVModelAssembler cvModelAssembler) {
        this.cvService = cvService;
        this.cvModelAssembler = cvModelAssembler;
    }

    @GetMapping
    @Operation(summary = "Get all CVs", description = "Retrieve a list of all CVs for the authenticated user.")
    public CollectionModel<EntityModel<CVResponse>> getAllCVs() {
        List<EntityModel<CVResponse>> cvs = cvService.getAllCVs().stream()
                .map(cvModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cvs, linkTo(methodOn(CVController.class).getAllCVs()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CV by ID", description = "Retrieve a CV by its ID.")
    public EntityModel<CVResponse> getCVById(@PathVariable Long id) {
        CVResponse cvResponse = cvService.getCVById(id)
                .orElseThrow(() -> new CVNotFoundException(id));
        return cvModelAssembler.toModel(cvResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new CV", description = "Add a new CV to the system.")
    public ResponseEntity<?> createCV(
            @RequestPart("cvRequest") CVRequest cvRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        String username = HelperUtils.getAuthenticatedUsername();
        CVResponse cvResponse = cvService.createCV(cvRequest, username, profileImage);
        EntityModel<CVResponse> cvModel = cvModelAssembler.toModel(cvResponse);
        return ResponseEntity
                .created(cvModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(cvModel);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update CV", description = "Update an existing CV or create a new one if not found.")
    public ResponseEntity<?> updateCV(
            @RequestPart("cvRequest") CVRequest cvRequest,
            @PathVariable Long id,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        String username = HelperUtils.getAuthenticatedUsername();
        CVResponse updatedCV = cvService.updateOrCreateCV(cvRequest, id, username, profileImage);
        EntityModel<CVResponse> cvModel = cvModelAssembler.toModel(updatedCV);
        return ResponseEntity
                .created(cvModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(cvModel);
    }

    @DeleteMapping
    @Operation(summary = "Delete CV", description = "Delete a CV by its ID or Full Name.")
    public ResponseEntity<Void> deleteCV(@RequestParam(required = false) Long id,
                                         @RequestParam(required = false) String fullName) {
        if (id != null) {
            cvService.deleteCV(id);
        } else if (fullName != null) {
            cvService.deleteByFullName(fullName);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/generate-pdf")
    @Operation(summary = "Generate PDF", description = "Generate a PDF for the CV.")
    public ResponseEntity<byte[]> generatePDF(@PathVariable Long id) {
        byte[] pdfBytes = cvService.generatePDF(id);

        CVResponse cv = cvService.getCVById(id)
                .orElseThrow(() -> new CVNotFoundException(id));

        String sanitizedFullName = cv.fullName().replaceAll("[^a-zA-Z0-9]", "_");
        String filename = "CV_" + sanitizedFullName + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}