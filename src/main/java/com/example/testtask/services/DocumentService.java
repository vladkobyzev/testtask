package com.example.testtask.services;

import com.example.testtask.dto.DocumentDto;
import com.example.testtask.dto.DocumentRequestDto;
import com.example.testtask.models.Document;
import com.example.testtask.repositoy.DocumentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentService {
    private final ModelMapper modelMapper;
    private final DocumentRepository documentRepository;
    public DocumentDto addDocument(DocumentRequestDto documentRequestDto) {
        Document parent = null;
        if (documentRequestDto.getParentId() != null) {
            parent = findDocumentById(documentRequestDto.getParentId());
        }

        Document document = new Document();
        document.setTitle(documentRequestDto.getName());
        document.setContent(documentRequestDto.getContent());
        document.setParent(parent);

        return convertEntityToDto(documentRepository.save(document));
    }

    public List<DocumentDto> getDocuments(long docId) {
        List<Document> hierarchy = new ArrayList<>();
        Document document = findDocumentById(docId);
        buildDocumentHierarchy(document, hierarchy);
        return hierarchy.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private Document findDocumentById(long docId) {
        return documentRepository.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
    }

    private void buildDocumentHierarchy(Document document, List<Document> hierarchy) {
        hierarchy.add(document);
        List<Document> children = documentRepository.findByParentOrderByTitleAsc(document);
        for (Document child : children) {
            buildDocumentHierarchy(child, hierarchy);
        }
    }

    private DocumentDto convertEntityToDto(Document document) {
        return modelMapper.map(document, DocumentDto.class);
    }
}
