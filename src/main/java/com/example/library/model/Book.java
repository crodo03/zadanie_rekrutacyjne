package com.example.library.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    public Book(String title, String author, String publishing, int numberOfPages) {
        this.title = title;
        this.author = author;
        this.publishing = publishing;
        this.numberOfPages = numberOfPages;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String author;
    private String publishing;
    private int numberOfPages;

}
