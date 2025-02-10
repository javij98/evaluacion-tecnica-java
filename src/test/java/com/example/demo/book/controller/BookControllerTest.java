package com.example.demo.book.controller;

import com.example.demo.book.dto.BookDTO;
import com.example.demo.book.exception.BookException;
import com.example.demo.book.service.BookService;
import com.example.demo.exceptions.models.ExceptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private List<BookDTO> bookDTOList;

    @BeforeEach
    void setUp() {

        bookDTOList = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {
            BookDTO bookDTO = new BookDTO(
                    Long.valueOf(Integer.valueOf(i).toString()),
                    "Book " + i,
                    "Author " + i,
                    "ISBN " + i,
                    LocalDate.now()
            );
            bookDTOList.add(bookDTO);
        }
    }

    @Test
    void getBooks() throws Exception {

        // Mock the service
        when(bookService.getBooks()).thenReturn(bookDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(bookDTOList.size())));
    }


    @Test
    void getBookById() throws Exception {

        // Mock the service
        when(bookService.getBookById(any(Long.class))).thenReturn(bookDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(bookDTOList.get(0).getTitle())))
                .andExpect(jsonPath("$.author", is(bookDTOList.get(0).getAuthor())))
                .andExpect(jsonPath("$.isbn", is(bookDTOList.get(0).getIsbn())))
                .andExpect(jsonPath("$.publicationDate", is(bookDTOList.get(0).getPublicationDate().toString())));

    }

    @Test
    void getBookByIdNotFoundException() throws Exception {

        // Mock the service
        when(bookService.getBookById(any(Long.class))).thenThrow(new BookException(new ExceptionEntity(501, "Book not found with id: 1")));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("Book not found with id: 1")));
    }

    @Test
    void createBook() throws Exception {
        BookDTO newBookDTO = new BookDTO(
                11L,
                "New Book",
                "New Author",
                "New ISBN",
                LocalDate.now()
        );

        // Mock the service
        when(bookService.createBook(any(BookDTO.class))).thenReturn(newBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType("application/json")
                .content("{\"id\":11,\"title\":\"New Book\",\"author\":\"New Author\",\"isbn\":\"New ISBN\",\"publicationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(newBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(newBookDTO.getAuthor())))
                .andExpect(jsonPath("$.isbn", is(newBookDTO.getIsbn())))
                .andExpect(jsonPath("$.publicationDate", is(newBookDTO.getPublicationDate().toString())));
    }

    @Test
    void updateBook() throws Exception {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(
                bookId,
                "Updated Book",
                "Updated Author",
                "Updated ISBN",
                LocalDate.now()
        );

        // Mock the service
        when(bookService.updateBook(any(Long.class), any(BookDTO.class))).thenReturn(updatedBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", bookId)
                .contentType("application/json")
                .content("{\"id\":1,\"title\":\"Updated Book\",\"author\":\"Updated Author\",\"isbn\":\"Updated ISBN\",\"publicationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(updatedBookDTO.getAuthor())))
                .andExpect(jsonPath("$.isbn", is(updatedBookDTO.getIsbn())))
                .andExpect(jsonPath("$.publicationDate", is(updatedBookDTO.getPublicationDate().toString())));
    }

    @Test
    void updateBookNotFoundException() throws Exception {
        Long bookId = 1L;

        // Mock the service
        when(bookService.updateBook(any(Long.class), any(BookDTO.class))).thenThrow(new BookException(new ExceptionEntity(501, "Book not found with id: " + bookId)));

        mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", bookId)
                        .contentType("application/json")
                        .content("{\"id\":1,\"title\":\"Updated Book\",\"author\":\"Updated Author\",\"isbn\":\"Updated ISBN\",\"publicationDate\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("Book not found with id: " + bookId)));
    }

    @Test
    void partiallyUpdateBook() throws Exception {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(
                bookId,
                "Partially Updated Book",
                "Updated Author",
                "Updated ISBN",
                LocalDate.now()
        );

        when(bookService.partiallyUpdateBook(any(Long.class), any(Map.class))).thenReturn(updatedBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/{id}", bookId)
                        .contentType("application/json")
                        .content("{\"title\":\"Partially Updated Book\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedBookDTO.getTitle())))
                .andExpect(jsonPath("$.author", is(updatedBookDTO.getAuthor())))
                .andExpect(jsonPath("$.isbn", is(updatedBookDTO.getIsbn())))
                .andExpect(jsonPath("$.publicationDate", is(updatedBookDTO.getPublicationDate().toString())));
    }

    @Test
    void partiallyUpdateBookNotFoundException() throws Exception {
        Long bookId = 1L;

        // Mock the service
        when(bookService.partiallyUpdateBook(any(Long.class), any(Map.class))).thenThrow(new BookException(new ExceptionEntity(501, "Book not found with id: " + bookId)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/{id}", bookId)
                        .contentType("application/json")
                        .content("{\"title\":\"Partially Updated Book\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(501)))
                .andExpect(jsonPath("$.message", is("Book not found with id: " + bookId)));
    }

    @Test
    void deleteBook() throws Exception {
        Long bookId = 1L;

        // Mock the service
        doNothing().when(bookService).deleteBook(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", bookId))
                .andExpect(status().isOk());
    }
}