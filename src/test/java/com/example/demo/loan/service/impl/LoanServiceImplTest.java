package com.example.demo.loan.service.impl;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.dto.BookDTO;
import com.example.demo.book.exception.BookException;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.loan.dao.LoanDAO;
import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.exception.LoanException;
import com.example.demo.loan.mapper.LoanMapper;
import com.example.demo.loan.repository.LoanRepository;
import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private LoanDAO exampleLoan;
    private List<LoanDAO> loanDAOList;
    private BookDAO exampleBook;
    private UserDAO exampleUser;

    private static final LocalDate LOAN_DATE = LocalDate.of(2023, 1, 1);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 1, 31);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        exampleBook = BookDAO.builder()
                .id(1L)
                .title("Book 1")
                .author("Author 1")
                .isbn("ISBN 1")
                .publicationDate(LocalDate.of(2020, 1, 1))
                .build();

        exampleUser = UserDAO.builder()
                .id(1L)
                .name("User 1")
                .phoneNumber("1234567890")
                .registrationDate(LocalDate.of(2020, 1, 1))
                .build();

        exampleLoan = LoanDAO.builder()
                .id(1L)
                .book(exampleBook)
                .user(exampleUser)
                .loanDate(LOAN_DATE)
                .returnDate(RETURN_DATE)
                .build();

        loanDAOList = List.of(exampleLoan);
    }

    @Test
    void getLoans() {
        when(loanRepository.findAll()).thenReturn(loanDAOList);

        List<LoanDTO> result = loanService.getLoans();

        assertNotNull(result);
        assertEquals(loanDAOList.size(), result.size());
    }

    @Test
    void getLoanById() {
        Long loanId = 1L;
        LoanDTO loanDTO = LoanDTO.builder()
                .id(exampleLoan.getId())
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(loanMapper.loanDAOToLoanDTO(exampleLoan)).thenReturn(loanDTO);

        LoanDTO result = loanService.getLoanById(loanId);

        assertNotNull(result);
        assertEquals(loanDTO, result);
    }

    @Test
    void getLoanByIdNotFoundException() {
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        LoanException exception = assertThrows(LoanException.class, () -> loanService.getLoanById(loanId));

        assertEquals("Loan not found with id: " + loanId, exception.getErr().getMessage());
    }

    @Test
    void createLoan() {
        LoanDTO loanDTO = LoanDTO.builder()
                .id(exampleLoan.getId())
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.of(exampleBook));
        when(userRepository.findById(exampleUser.getId())).thenReturn(Optional.of(exampleUser));
        when(loanMapper.loanDTOToLoanDAO(loanDTO)).thenReturn(exampleLoan);
        when(loanRepository.save(exampleLoan)).thenReturn(exampleLoan);
        when(loanMapper.loanDAOToLoanDTO(exampleLoan)).thenReturn(loanDTO);

        LoanDTO result = loanService.createLoan(loanDTO);

        assertNotNull(result);
        assertEquals(loanDTO, result);
    }

    @Test
    void createLoanBookNotFoundException() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.empty());

        BookException exception = assertThrows(BookException.class, () -> loanService.createLoan(loanDTO));

        assertEquals("Book not found with id: " + exampleBook.getId(), exception.getErr().getMessage());
    }

    @Test
    void createLoanUserNotFoundException() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.of(exampleBook));
        when(userRepository.findById(exampleUser.getId())).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> loanService.createLoan(loanDTO));

        assertEquals("User not found with id: " + exampleUser.getId(), exception.getErr().getMessage());
    }

    @Test
    void updateLoan() {
        Long loanId = exampleLoan.getId();
        LoanDTO loanDTO = LoanDTO.builder()
                .id(loanId)
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.of(exampleBook));
        when(userRepository.findById(exampleUser.getId())).thenReturn(Optional.of(exampleUser));
        when(loanRepository.save(exampleLoan)).thenReturn(exampleLoan);
        when(loanMapper.loanDAOToLoanDTO(exampleLoan)).thenReturn(loanDTO);

        LoanDTO result = loanService.updateLoan(loanId, loanDTO);

        assertNotNull(result);
        assertEquals(loanDTO, result);
    }

    @Test
    void updateLoanNotFoundException() {
        Long loanId = 1L;
        LoanDTO loanDTO = LoanDTO.builder()
                .id(loanId)
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        LoanException exception = assertThrows(LoanException.class, () -> loanService.updateLoan(loanId, loanDTO));

        assertEquals("Loan not found with id: " + loanId, exception.getErr().getMessage());
    }

    @Test
    void updateLoanBookNotFoundException() {
        Long loanId = exampleLoan.getId();
        LoanDTO loanDTO = LoanDTO.builder()
                .id(loanId)
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.empty());

        BookException exception = assertThrows(BookException.class, () -> loanService.updateLoan(loanId, loanDTO));

        assertEquals("Book not found with id: " + exampleBook.getId(), exception.getErr().getMessage());
    }

    @Test
    void updateLoanUserNotFoundException() {
        Long loanId = exampleLoan.getId();
        LoanDTO loanDTO = LoanDTO.builder()
                .id(loanId)
                .book(BookDTO.builder()
                        .id(exampleBook.getId())
                        .title(exampleBook.getTitle())
                        .author(exampleBook.getAuthor())
                        .isbn(exampleBook.getIsbn())
                        .publicationDate(exampleBook.getPublicationDate())
                        .build())
                .user(UserDTO.builder()
                        .id(exampleUser.getId())
                        .name(exampleUser.getName())
                        .phoneNumber(exampleUser.getPhoneNumber())
                        .registrationDate(exampleUser.getRegistrationDate())
                        .build())
                .loanDate(exampleLoan.getLoanDate())
                .returnDate(exampleLoan.getReturnDate())
                .build();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(bookRepository.findById(exampleBook.getId())).thenReturn(Optional.of(exampleBook));
        when(userRepository.findById(exampleUser.getId())).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> loanService.updateLoan(loanId, loanDTO));

        assertEquals("User not found with id: " + exampleUser.getId(), exception.getErr().getMessage());
    }

    @Test
    void partiallyUpdateLoanLoanDate() {
        Long loanId = exampleLoan.getId();
        LocalDate newLoanDate = LocalDate.of(2023, 2, 1);
        Map<String, Object> updates = Map.of("loanDate", newLoanDate);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(loanRepository.save(any(LoanDAO.class))).thenReturn(exampleLoan);

        LoanDTO result = loanService.partiallyUpdateLoan(loanId, updates);

        assertNotNull(result);
        assertEquals(newLoanDate, result.getLoanDate());
    }

    @Test
    void partiallyUpdateLoanReturnDate() {
        Long loanId = exampleLoan.getId();
        LocalDate newReturnDate = LocalDate.of(2023, 2, 28);
        Map<String, Object> updates = Map.of("returnDate", newReturnDate);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(loanRepository.save(any(LoanDAO.class))).thenReturn(exampleLoan);

        LoanDTO result = loanService.partiallyUpdateLoan(loanId, updates);

        assertNotNull(result);
        assertEquals(newReturnDate, result.getReturnDate());
    }

    @Test
    void partiallyUpdateLoanBookId() {
        Long loanId = exampleLoan.getId();
        Long newBookId = 2L;
        BookDAO newBook = BookDAO.builder().id(newBookId).title("New Book").author("New Author").isbn("New ISBN").publicationDate(LocalDate.of(2021, 1, 1)).build();
        Map<String, Object> updates = Map.of("bookId", newBookId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(bookRepository.findById(newBookId)).thenReturn(Optional.of(newBook));
        when(loanRepository.save(any(LoanDAO.class))).thenReturn(exampleLoan);

        LoanDTO result = loanService.partiallyUpdateLoan(loanId, updates);

        assertNotNull(result);
        assertEquals(newBookId, result.getBook().getId());
    }

    @Test
    void partiallyUpdateLoanUserId() {
        Long loanId = exampleLoan.getId();
        Long newUserId = 2L;
        UserDAO newUser = UserDAO.builder().id(newUserId).name("New User").phoneNumber("0987654321").registrationDate(LocalDate.of(2021, 1, 1)).build();
        Map<String, Object> updates = Map.of("userId", newUserId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(exampleLoan));
        when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
        when(loanRepository.save(any(LoanDAO.class))).thenReturn(exampleLoan);

        LoanDTO result = loanService.partiallyUpdateLoan(loanId, updates);

        assertNotNull(result);
        assertEquals(newUserId, result.getUser().getId());
    }

    @Test
    void partiallyUpdateLoanNotFoundException() {
        Long loanId = 1L;
        Map<String, Object> updates = Map.of("loanDate", LocalDate.of(2023, 2, 1));

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        LoanException exception = assertThrows(LoanException.class, () -> loanService.partiallyUpdateLoan(loanId, updates));

        assertEquals("Loan not found with id: " + loanId, exception.getErr().getMessage());
    }

    @Test
    void deleteLoan() {
        Long loanId = exampleLoan.getId();

        when(loanRepository.existsById(loanId)).thenReturn(true);
        doNothing().when(loanRepository).deleteById(loanId);

        assertDoesNotThrow(() -> loanService.deleteLoan(loanId));

        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void deleteLoanNotFoundException() {
        Long loanId = 1L;

        when(loanRepository.existsById(loanId)).thenReturn(false);

        LoanException exception = assertThrows(LoanException.class, () -> loanService.deleteLoan(loanId));

        assertEquals("Loan not found with id: " + loanId, exception.getErr().getMessage());
        verify(loanRepository, never()).deleteById(loanId);
    }

    @Test
    void validateLoanWithNullBook() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(null)
                .user(UserDTO.builder().id(1L).build())
                .loanDate(LocalDate.of(2023, 1, 1))
                .returnDate(LocalDate.of(2023, 1, 31))
                .build();

        LoanException exception = assertThrows(LoanException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("The loan must have an assigned book", exception.getErr().getMessage());
    }

    @Test
    void validateLoanWithNullUser() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder().id(1L).build())
                .user(null)
                .loanDate(LocalDate.of(2023, 1, 1))
                .returnDate(LocalDate.of(2023, 1, 31))
                .build();

        LoanException exception = assertThrows(LoanException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("The loan must have an assigned user", exception.getErr().getMessage());
    }

    @Test
    void validateLoanWithNullLoanDate() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder().id(1L).build())
                .user(UserDTO.builder().id(1L).build())
                .loanDate(null)
                .returnDate(LocalDate.of(2023, 1, 31))
                .build();

        LoanException exception = assertThrows(LoanException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("The loan date cannot be empty", exception.getErr().getMessage());
    }

    @Test
    void validateLoanWithNullReturnDate() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder().id(1L).build())
                .user(UserDTO.builder().id(1L).build())
                .loanDate(LocalDate.of(2023, 1, 1))
                .returnDate(null)
                .build();

        LoanException exception = assertThrows(LoanException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("The return date cannot be empty", exception.getErr().getMessage());
    }

    @Test
    void validateLoanWithReturnDateBeforeLoanDate() {
        LoanDTO loanDTO = LoanDTO.builder()
                .book(BookDTO.builder().id(1L).build())
                .user(UserDTO.builder().id(1L).build())
                .loanDate(LocalDate.of(2023, 1, 31))
                .returnDate(LocalDate.of(2023, 1, 1))
                .build();

        LoanException exception = assertThrows(LoanException.class, () -> loanService.createLoan(loanDTO));
        assertEquals("The return date cannot be before the loan date", exception.getErr().getMessage());
    }
}