package com.example.library;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookServiceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void setup() {
		bookRepository.deleteAll();
		bookRepository.saveAll(
				List.of(
					new Book("title1", "author1", "publishing1", 1),
					new Book("title2", "author2", "publishing2", 2)
				)
		);
	}


	@Test
	void shouldReturnAllBooksFromService() throws Exception {
		mockMvc.perform(get("/api/book/all-books"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].title").value("title1"))
				.andExpect(jsonPath("$[1].title").value("title2"));
	}

	@Test
	void testAddBook() {
		BookRequest bookRequest = new BookRequest(
				"Klara and the Sun",
				"Kazuo Ishiguro",
				"Faber and Faber",
				307
		);

		BookResponse bookResponse = bookService.addBook(bookRequest);

		assertEquals("book has been added", bookResponse.getMessage());
	}

	@Test
	void testDeleteBookById() {
		Book book = new Book("book", "to", "delete", 1);
		book = bookRepository.save(book);
		int bookId = book.getId();

		BookResponse bookResponse = bookService.deleteBookById(bookId);

		assertAll(
				() -> assertEquals("book with id " + bookId + " has been deleted", bookResponse.getMessage()),
				() -> assertFalse(bookRepository.findById(bookId).isPresent())
		);
	}

	@Test
	void testUpdateBookPagesById() {
		Book book = new Book(
				"Ring",
				"Stephen Baxter",
				"HarperCollins",
				400
		);
		book = bookRepository.save(book);
		int bookId = book.getId();

		BookResponse bookResponse = bookService.updateBookPagesById(bookId, 443);
		Book updatedBook = bookRepository.findById(bookId).orElseThrow();

		assertAll(
				() -> assertEquals("book with id " + bookId + " has been updated", bookResponse.getMessage()),
				() -> assertEquals(443, updatedBook.getNumberOfPages())
		);
	}
}
