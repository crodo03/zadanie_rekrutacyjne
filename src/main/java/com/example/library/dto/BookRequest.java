package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private String publishing;
    private int numberOfPages;
}
