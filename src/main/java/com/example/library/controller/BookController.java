package com.example.library.controller;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/all-books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/add")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.addBook(bookRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable int id) {
        return ResponseEntity.ok(bookService.deleteBookById(id));
    }

    @PutMapping("/update/{id}/pages")
    public ResponseEntity<BookResponse> updateBooksPages(
            @PathVariable int id,
            @RequestParam int numberOfPages
    ) {
        return ResponseEntity.ok(bookService.updateBookPagesById(id, numberOfPages));
    }
}
