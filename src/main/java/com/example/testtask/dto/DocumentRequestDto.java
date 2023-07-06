package com.example.testtask.dto;

import lombok.Data;

@Data
public class DocumentRequestDto {
    private String name;
    private String content;
    private Long parentId;
}
