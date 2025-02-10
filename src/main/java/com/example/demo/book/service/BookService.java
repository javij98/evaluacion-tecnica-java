package com.example.demo.book.service;

import com.example.demo.book.dto.BookDTO;

import java.util.List;
import java.util.Map;

public interface BookService {
    List<BookDTO> getBooks();
    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    BookDTO partiallyUpdateBook(Long id, Map<String, String> updates);
    void deleteBook(Long id);
}
