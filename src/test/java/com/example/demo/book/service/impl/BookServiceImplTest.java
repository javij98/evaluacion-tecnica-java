package com.example.demo.book.service.impl;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.dto.BookDTO;
import com.example.demo.book.mapper.BookMapper;
import com.example.demo.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private List<BookDAO> bookDAOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        bookDAOList = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {
            BookDAO bookDAO = BookDAO.builder()
                    .id(Long.valueOf(Integer.valueOf(i).toString()))
                    .title("Book " + i)
                    .author("Author " + i)
                    .isbn("ISBN " + i)
                    .publicationDate(LocalDate.now())
                    .build();

            bookDAOList.add(bookDAO);
        }
    }

    @Test
    void getBooks() {

        when(bookRepository.findAll()).thenReturn(bookDAOList);
        List<BookDTO> listOfBookDTO = bookService.getBooks();
        assertEquals(bookDAOList.size(), listOfBookDTO.size());
    }

    @Test
    void getBookById() {

        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookDAOList.get(0)));
        BookDTO bookDTO = bookService.getBookById(1L);
        assertEquals(bookDAOList.get(0).getId(), bookDTO.getId());
        assertEquals(bookDAOList.get(0).getTitle(), bookDTO.getTitle());
        assertEquals(bookDAOList.get(0).getAuthor(), bookDTO.getAuthor());
        assertEquals(bookDAOList.get(0).getIsbn(), bookDTO.getIsbn());
        assertEquals(bookDAOList.get(0).getPublicationDate(), bookDTO.getPublicationDate());
    }

    @Test
    void createBook() {

        // Arrange
        BookDTO newBookDTO = BookDTO.builder()
                .title("New Book")
                .author("New Author")
                .isbn("New ISBN")
                .publicationDate(LocalDate.now())
                .build();

        BookDAO newBookDAO = BookDAO.builder()
                .title("New Book")
                .author("New Author")
                .isbn("New ISBN")
                .publicationDate(LocalDate.now())
                .build();

        BookDAO createdBookDAO = BookDAO.builder()
                .id(11L)
                .title("New Book")
                .author("New Author")
                .isbn("New ISBN")
                .publicationDate(LocalDate.now())
                .build();

        BookDTO createdBookDTO = new BookDTO(
                11L,
                "New Book",
                "New Author",
                "New ISBN",
                LocalDate.now());

        when(bookMapper.bookDTOToBookDAO(newBookDTO)).thenReturn(newBookDAO);
        when(bookRepository.save(newBookDAO)).thenReturn(createdBookDAO);
        when(bookMapper.bookDAOToBookDTO(createdBookDAO)).thenReturn(createdBookDTO);

        // Act
        BookDTO result = bookService.createBook(newBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(11L, result.getId());
        assertEquals("New Book", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals("New ISBN", result.getIsbn());
        assertEquals(LocalDate.now(), result.getPublicationDate());
    }

    @Test
    void updateBook() {
        Long bookId = 1L;
        BookDTO updatedBookDTO = BookDTO.builder()
                .id(bookId)
                .title("Updated Book")
                .author("Updated Author")
                .isbn("Updated ISBN")
                .publicationDate(LocalDate.now())
                .build();

        BookDAO existingBookDAO = bookDAOList.get(0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBookDAO));
        when(bookRepository.save(existingBookDAO)).thenReturn(existingBookDAO);
        when(bookMapper.bookDAOToBookDTO(existingBookDAO)).thenReturn(updatedBookDTO);

        BookDTO result = bookService.updateBook(bookId, updatedBookDTO);

        assertNotNull(result);
        assertEquals(updatedBookDTO.getId(), result.getId());
        assertEquals(updatedBookDTO.getTitle(), result.getTitle());
        assertEquals(updatedBookDTO.getAuthor(), result.getAuthor());
        assertEquals(updatedBookDTO.getIsbn(), result.getIsbn());
        assertEquals(updatedBookDTO.getPublicationDate(), result.getPublicationDate());
    }

    @Test
    void partiallyUpdateBook_AllFields() {
        Long bookId = 1L;
        Map<String, String> updates = new HashMap<>();
        updates.put("title", "Fully Updated Book");
        updates.put("author", "Fully Updated Author");
        updates.put("isbn", "Updated ISBN 1234");
        updates.put("publicationDate", LocalDate.of(2025, 5, 20).toString());

        BookDAO existingBookDAO = bookDAOList.get(0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBookDAO));
        when(bookRepository.save(existingBookDAO)).thenReturn(existingBookDAO);

        BookDTO updatedBookDTO = BookDTO.builder()
                .id(bookId)
                .title("Fully Updated Book")
                .author("Fully Updated Author")
                .isbn("Updated ISBN 1234")
                .publicationDate(LocalDate.of(2025, 5, 20))
                .build();

        when(bookMapper.bookDAOToBookDTO(existingBookDAO)).thenReturn(updatedBookDTO);

        BookDTO result = bookService.partiallyUpdateBook(bookId, updates);

        assertNotNull(result);
        assertEquals("Fully Updated Book", result.getTitle());
        assertEquals("Fully Updated Author", result.getAuthor());
        assertEquals("Updated ISBN 1234", result.getIsbn());
        assertEquals(LocalDate.of(2025, 5, 20), result.getPublicationDate());
    }

    @Test
    void deleteBook() {

        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteBook(bookId);
    }
}