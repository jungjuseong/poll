package com.clbee.pagebuilder.service;

import com.clbee.pagebuilder.exception.BadRequestException;
import com.clbee.pagebuilder.exception.ResourceNotFoundException;
import com.clbee.pagebuilder.model.*;
import com.clbee.pagebuilder.payload.PagedResponse;
import com.clbee.pagebuilder.payload.DocumentRequest;
import com.clbee.pagebuilder.payload.DocumentResponse;
import com.clbee.pagebuilder.repository.DocumentRepository;
import com.clbee.pagebuilder.repository.UserRepository;
import com.clbee.pagebuilder.util.AppConstants;
import com.clbee.pagebuilder.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentService(DocumentRepository documentRepository, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        logger.info("Autowired DocumentService.");
    }

    public Long getDocumentCountByCreatedBy(Long userId) {
        return documentRepository.countByCreatedBy(userId);
    }

    public PagedResponse<DocumentResponse> getAllDocuments(int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Documents
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Document> documents = documentRepository.findAll(pageable);

        if(documents.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), documents.getNumber(),
                    documents.getSize(), documents.getTotalElements(), documents.getTotalPages(), documents.isLast());
        }

        Map<Long, User> creatorMap = getDocumentCreatorMap(documents.getContent());

        List<DocumentResponse> response = documents.map(document -> {
            return ModelMapper.mapDocumentToDocumentResponse(document, creatorMap.get(document.getCreatedBy()));
        }).getContent();

        logger.info("document-response: " + String.valueOf(response));

        return new PagedResponse<>(response, documents.getNumber(),
                documents.getSize(), documents.getTotalElements(), documents.getTotalPages(), documents.isLast());
    }

    public PagedResponse<DocumentResponse> getDocumentsCreatedBy(String username, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all documents created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Document> documents = documentRepository.findByCreatedBy(user.getId(), pageable);

        if (documents.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), documents.getNumber(),
                    documents.getSize(), documents.getTotalElements(), documents.getTotalPages(), documents.isLast());
        }

        List<DocumentResponse> documentResponses = documents.map(document -> {
            return ModelMapper.mapDocumentToDocumentResponse(document, user);
        }).getContent();

        return new PagedResponse<>(documentResponses, documents.getNumber(),
                documents.getSize(), documents.getTotalElements(), documents.getTotalPages(), documents.isLast());
    }

    public Document createDocument(DocumentRequest request) {
        Document document = new Document();

        document.setName(request.getName());
        document.setDeadmark(request.getDeadmark());
        document.setContents(request.getContents());
        document.setPreference(request.getPreference());

        return documentRepository.save(document);
    }

    public Optional<Document> updateDocument(Long id, DocumentRequest request) {

        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isPresent()) {
            optionalDocument.get().setName(request.getName());
            optionalDocument.get().setDeadmark(request.getDeadmark());
            optionalDocument.get().setContents(request.getContents());
            optionalDocument.get().setPreference(request.getPreference());

            documentRepository.save(optionalDocument.get());
        }

        return optionalDocument;
    }

    public DocumentResponse getDocumentById(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(
                () -> new ResourceNotFoundException("Document", "id", documentId));

        // Retrieve document creator details
        User creator = userRepository.findById(document.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", document.getCreatedBy()));

        return ModelMapper.mapDocumentToDocumentResponse(document, creator);
    }

    public Boolean deleteDocument(Long id) {

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Document", "id", id));

        documentRepository.delete(document);
        return true;
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Map<Long, User> getDocumentCreatorMap(List<Document> documents) {
        /* Get Document Creator details of the given list of documents */
        List<Long> creatorIds = documents.stream()
                .map(Document::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
