package com.example.demo.book.service.impl;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.dto.BookDTO;
import com.example.demo.book.exception.BookException;
import com.example.demo.exceptions.models.ExceptionEntity;
import com.example.demo.book.mapper.BookMapper;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.book.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    private static final BookMapper bookMapper = BookMapper.INSTANCE;

    @Override
    public List<BookDTO> getBooks() {
        logger.debug("LibroServiceImpl: Getting all books...");
        List<BookDAO> listOfBooksDAO = bookRepository.findAll();
        List<BookDTO> listOfBooksDTOs = bookMapper.bookDAOsToBookDTOs(listOfBooksDAO);
        logger.debug("BookServiceImpl: getBooks() -> {} books obtained.", listOfBooksDAO.size());
        return listOfBooksDTOs;
    }

    @Override
    public BookDTO getBookById(Long id) throws BookException {
        logger.debug("BookServiceImpl: Getting book with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "Book not found with id: " + id);
        BookDAO bookDAO = bookRepository.findById(id).orElseThrow(() -> new BookException(err));
        BookDTO bookDTO = bookMapper.bookDAOToBookDTO(bookDAO);
        logger.debug("BookServiceImpl: getBookById() -> Book obtained: {}", bookDTO);
        return bookDTO;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        logger.debug("BookServiceImpl: Creating a new book...");
        BookDAO bookDAO = bookMapper.bookDTOToBookDAO(bookDTO);
        BookDAO createdBookDAO = bookRepository.save(bookDAO);
        BookDTO createdBookDTO = bookMapper.bookDAOToBookDTO(createdBookDAO);
        logger.debug("BookServiceImpl: createBook() -> Book created: {}", createdBookDTO);
        return createdBookDTO;
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        logger.debug("BookServiceImpl: Updating book with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "Book not found with id: " + id);
        BookDAO bookDAO = bookRepository.findById(id).orElseThrow(() -> new BookException(err));

        bookDAO.setTitle(bookDTO.getTitle());
        bookDAO.setAuthor(bookDTO.getAuthor());
        bookDAO.setIsbn(bookDTO.getIsbn());
        bookDAO.setPublicationDate(bookDTO.getPublicationDate());
        BookDAO updatedBookDAO = bookRepository.save(bookDAO);
        BookDTO updatedBookDTO = bookMapper.bookDAOToBookDTO(updatedBookDAO);

        logger.debug("BookServiceImpl: updateBook() -> Book updated: {}", updatedBookDTO);
        return updatedBookDTO;
    }

    @Override
    public BookDTO partiallyUpdateBook(Long id, Map<String, String> updates) {
        logger.debug("BookServiceImpl: Partially updating book with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(501, "Book not found with id: " + id);
        BookDAO bookDAO = bookRepository.findById(id).orElseThrow(() -> new BookException(err));

        if (updates.containsKey("title")) {
            bookDAO.setTitle(updates.get("title"));
        }
        if (updates.containsKey("author")) {
            bookDAO.setAuthor(updates.get("author"));
        }
        if (updates.containsKey("isbn")) {
            bookDAO.setIsbn(updates.get("isbn"));
        }
        if (updates.containsKey("publicationDate")) {
            bookDAO.setPublicationDate(LocalDate.parse(updates.get("publicationDate")));
        }

        BookDAO updatedBookDAO = bookRepository.save(bookDAO);
        BookDTO updatedBookDTO = bookMapper.bookDAOToBookDTO(updatedBookDAO);

        logger.debug("BookServiceImpl: partiallyUpdateBook() -> Book updated: {}", updatedBookDTO);
        return updatedBookDTO;
    }


    @Override
    public void deleteBook(Long id) {
        logger.debug("BookServiceImpl: Deleting book with id {}...", id);
        bookRepository.deleteById(id);
        logger.debug("BookServiceImpl: deleteBook() -> Book deleted with id: {}", id);
    }
}
