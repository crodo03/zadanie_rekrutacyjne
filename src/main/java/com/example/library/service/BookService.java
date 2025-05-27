package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.dto.BookDTO;
import com.example.library.mapper.DTOMapper;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final DTOMapper mapper;

    public List<BookDTO> getAllBooks() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public BookResponse addBook(BookRequest bookRequest) {
        Book book = new Book(
                bookRequest.getTitle(),
                bookRequest.getAuthor(),
                bookRequest.getPublishing(),
                bookRequest.getNumberOfPages()
        );
        repository.save(book);
        return new BookResponse("book has been added");
    }

    public BookResponse deleteBookById(int id) {
        getBookById(id);
        repository.deleteById(id);
        return new BookResponse("book with id " + id + " has been deleted");
    }

    public BookResponse updateBookPagesById(int id, int numberOfPages) {
        Book book = getBookById(id);
        book.setNumberOfPages(numberOfPages);
        repository.save(book);

        return new BookResponse("book with id " + id + " has been updated");
    }

    private Book getBookById(int id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book with id " + id + " does not exist"));
    }
}
