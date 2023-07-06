package com.example.testtask.repositoy;

import com.example.testtask.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByParentOrderByTitleAsc(Document parent);

}
