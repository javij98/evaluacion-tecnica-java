package com.example.demo.book.controller;

import com.example.demo.book.dto.BookDTO;
import com.example.demo.book.exception.BookException;
import com.example.demo.book.service.BookService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;

    @GetMapping
    List<BookDTO> getBooks() {
        logger.info("Getting all books...");
        List<BookDTO> listOfBooks = bookService.getBooks();
        logger.info("BookController: getBooks() -> {} books obtained.", listOfBooks);
        return listOfBooks;
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getBookById(@PathVariable Long id) {
        logger.info("Getting book with id {}...", id);
        try {
            BookDTO book = bookService.getBookById(id);
            logger.info("BookController: getBookById() -> Book obtained: {}", book);
            return ResponseEntity.ok(book);
        } catch (BookException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PostMapping
    BookDTO createBook(@RequestBody BookDTO bookDTO) {
        logger.info("Creating a new book...");
        BookDTO createdBook = bookService.createBook(bookDTO);
        logger.info("BookController: createBook() -> Book created: {}", createdBook);
        return createdBook;
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        logger.info("Updating book with id {}...", id);
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            logger.info("BookController: updateBook() -> Book updated: {}", updatedBook);
            return ResponseEntity.ok(updatedBook);
        } catch (BookException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<Object> partiallyUpdateBook(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        logger.info("Partially updating book with id {}...", id);
        try {
            BookDTO updatedBook = bookService.partiallyUpdateBook(id, updates);
            logger.info("BookController: partiallyUpdateBook() -> Book updated: {}", updatedBook);
            return ResponseEntity.ok(updatedBook);
        } catch (BookException e) {
            logger.error("BookController: Error partially updating book with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable Long id) {
        logger.info("Deleting book with id {}...", id);
        bookService.deleteBook(id);
        logger.info("BookController: deleteBook() -> Book deleted with id: {}", id);
    }
}
