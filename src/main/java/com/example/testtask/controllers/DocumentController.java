package com.example.testtask.controllers;

import com.example.testtask.dto.DocumentDto;
import com.example.testtask.dto.DocumentRequestDto;
import com.example.testtask.services.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    @PostMapping
    public DocumentDto addDocument(@RequestBody DocumentRequestDto documentRequestDto) {
        return documentService.addDocument(documentRequestDto);
    }

    @GetMapping("/{docId}")
    public List<DocumentDto> getDocuments(@PathVariable long docId) {
        return documentService.getDocuments(docId);
    }
}
