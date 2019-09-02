package com.clbee.pagebuilder.repository;

import com.clbee.pagebuilder.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findById(Long documentId);
    Optional<Document> findByName(String documentName);

    Page<Document> findByCreatedBy(Long userId, Pageable pageable);
    Page<Document> findByCreatedByOrderByNameDesc(Long userId, Pageable pageable);
    Page<Document> findByCreatedByAndName(Long userid, String name, Pageable pageable);

    Long countByCreatedBy(Long userId);

    Boolean existsByName(String name);

    List<Document> findByIdIn(List<Long> documentids);

    List<Document> findByIdIn(List<Long> documentids, Sort sort);
}
