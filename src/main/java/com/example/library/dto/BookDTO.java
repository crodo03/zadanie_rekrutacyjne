package com.example.library.dto;

public record BookDTO(
        String title,
        String author,
        String publishing,
        int numberOfPages
) {}
