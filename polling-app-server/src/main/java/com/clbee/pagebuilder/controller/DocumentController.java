package com.clbee.pagebuilder.controller;

import com.clbee.pagebuilder.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clbee.pagebuilder.model.Document;
import com.clbee.pagebuilder.payload.*;

import com.clbee.pagebuilder.service.DocumentService;
import com.clbee.pagebuilder.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import javax.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/documents")
    public PagedResponse<DocumentResponse> getAllDocuments(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return documentService.getAllDocuments(page, size);
    }

    @GetMapping("/documents/{document_id}")
    public DocumentResponse getDocumentById(@PathVariable Long document_id) {
        return documentService.getDocumentById(document_id);
    }

    @PostMapping("/documents")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createDocument(@Valid @RequestBody DocumentRequest request) {
        Document document = documentService.createDocument(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{documentId}")
                .buildAndExpand(document.getId()).toUri();

        logger.info("Document Created Successfully");

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Document Created Successfully",document.getId()));
    }

    @PutMapping("/documents//{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateDocumentById(@PathVariable Long id, @Valid @RequestBody DocumentRequest request) {
        Optional<Document> result = documentService.updateDocument(id, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        logger.info("Document updated Successfully");

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Document updated Successfully", id));
    }

    @DeleteMapping("/documents//{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteDocumentById(@PathVariable Long id) {
        Boolean result = documentService.deleteDocument(id);

        logger.info("Document Deleted Successfully");

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Document Deleted Successfully", -1L));
    }

    @GetMapping("/documents//checkDocumentNameAvailability")
    public DocumentNameIdentityAvailability checkDocumentNameAvailability(@RequestParam(value = "name") String name) {
        Boolean isAvailable = !documentRepository.existsByName(name);

        return new DocumentNameIdentityAvailability(isAvailable);
    }
}
