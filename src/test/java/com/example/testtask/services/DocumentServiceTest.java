package com.example.testtask.services;

import com.example.testtask.dto.DocumentDto;
import com.example.testtask.dto.DocumentRequestDto;
import com.example.testtask.models.Document;
import com.example.testtask.repositoy.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DocumentService documentService;

    @Test()
    public void testGetDocumentThrowsIllegalArgumentException() {
        long invalidId = -1;
        assertThrows(IllegalArgumentException.class, () -> documentService.getDocuments(invalidId));
    }

    @Test()
    public void testAddDocumentThrowsIllegalArgumentException() {
        long invalidId = -1;
        DocumentRequestDto documentRequestDto = new DocumentRequestDto();
        documentRequestDto.setParentId(invalidId);
        assertThrows(IllegalArgumentException.class, () -> documentService.addDocument(documentRequestDto));
    }

    @Test()
    public void testGetDocumentSuccess() {
        Document document = new Document();
        document.setId(1L);
        List<Document> childrens = new ArrayList<>();
        Document children = new Document();
        children.setId(2L);
        childrens.add(children);
        DocumentDto childrenDto1 = new DocumentDto();
        childrenDto1.setName("doc1");
        DocumentDto childrenDto2 = new DocumentDto();
        childrenDto2.setName("doc2");

        List<DocumentDto> hierarchy = List.of(childrenDto1, childrenDto2);
        long docId = 1;

        when(documentRepository.findById(docId)).thenReturn(Optional.of(document));
        when(documentRepository.findByParentOrderByTitleAsc(document)).thenReturn(childrens);
        when(modelMapper.map(document, DocumentDto.class)).thenReturn(childrenDto1);
        when(modelMapper.map(children, DocumentDto.class)).thenReturn(childrenDto2);



        List<DocumentDto> result = documentService.getDocuments(docId);

        assertEquals(hierarchy.size(), result.size());
        assertEquals(hierarchy.get(0).getName(), result.get(0).getName());
        assertEquals(hierarchy.get(1).getName(), result.get(1).getName());
    }

    @Test()
    public void testAddDocumentSuccess() {
        long parentId = 1L;
        Document parent = new Document();
        parent.setId(parentId);
        Document document = new Document();
        document.setContent("test");
        document.setParent(parent);
        DocumentRequestDto documentRequestDto = new DocumentRequestDto();
        documentRequestDto.setContent("test");
        documentRequestDto.setParentId(parentId);
        DocumentDto documentDto = new DocumentDto();
        documentDto.setContent("test");

        when(documentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        when(modelMapper.map(document, DocumentDto.class)).thenReturn(documentDto);

        DocumentDto result = documentService.addDocument(documentRequestDto);

        assertEquals(documentRequestDto.getContent(), result.getContent());

    }
}
