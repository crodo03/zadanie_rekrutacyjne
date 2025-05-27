package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.model.Book;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public BookDTO toDto(Book book) {
        return new BookDTO(
            book.getTitle(),
            book.getAuthor(),
            book.getPublishing(),
            book.getNumberOfPages()
        );
    }
}
