package com.example.demo.loan.service.impl;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.exception.BookException;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.exceptions.models.ExceptionEntity;
import com.example.demo.loan.dao.LoanDAO;
import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.exception.LoanException;
import com.example.demo.loan.mapper.LoanMapper;
import com.example.demo.loan.repository.LoanRepository;
import com.example.demo.loan.service.LoanService;
import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.exception.UserException;
import com.example.demo.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private static final LoanMapper loanMapper = LoanMapper.INSTANCE;

    private void validateLoan(LoanDTO loanDTO) {
        if (loanDTO.getBook() == null || loanDTO.getBook().getId() == null) {
            throw new LoanException(new ExceptionEntity(400, "The loan must have an assigned book"));
        }
        if (loanDTO.getUser() == null || loanDTO.getUser().getId() == null) {
            throw new LoanException(new ExceptionEntity(400, "The loan must have an assigned user"));
        }
        if (loanDTO.getLoanDate() == null) {
            throw new LoanException(new ExceptionEntity(400, "The loan date cannot be empty"));
        }
        if (loanDTO.getReturnDate() == null) {
            throw new LoanException(new ExceptionEntity(400, "The return date cannot be empty"));
        }
        if (loanDTO.getReturnDate().isBefore(loanDTO.getLoanDate())) {
            throw new LoanException(new ExceptionEntity(400, "The return date cannot be before the loan date"));
        }
    }

//    private void validateBookNotOnLoan(Long bookId) {
//        boolean bookOnLoan = loanRepository.findByBookId(bookId).stream()
//                .anyMatch(loan -> loan.getReturnDate().isAfter(LocalDate.now()));
//
//        if (bookOnLoan) {
//            logger.warn("[Service] The book with id {} is currently on loan", bookId);
//            throw new LoanException(new ExceptionEntity(400, "The book is already on loan and cannot be assigned again."));
//        }
//    }

    @Override
    public List<LoanDTO> getLoans() {
        logger.debug("LoanServiceImpl: Getting all loans...");
        List<LoanDAO> listOfLoansDAO = loanRepository.findAll();
        List<LoanDTO> listOfLoansDTOs = loanMapper.loanDAOsToLoanDTOs(listOfLoansDAO);
        logger.debug("LoanServiceImpl: getLoans() -> {} loans obtained.", listOfLoansDAO.size());
        return listOfLoansDTOs;
    }

    @Override
    public LoanDTO getLoanById(Long id) {
        logger.debug("LoanServiceImpl: Getting loan with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(404, "Loan not found with id: " + id);
        LoanDAO loanDAO = loanRepository.findById(id).orElseThrow(() -> {
            logger.warn("LoanServiceImpl: getLoanById() -> Loan not found with id: {}", id);
            return new LoanException(err);
        });
        LoanDTO loanDTO = loanMapper.loanDAOToLoanDTO(loanDAO);
        logger.debug("LoanServiceImpl: getLoanById() -> Loan obtained: {}", loanDTO);
        return loanDTO;
    }

    @Override
    public LoanDTO createLoan(LoanDTO loanDTO) {
        logger.info("[Service] Creating a new loan");

        validateLoan(loanDTO);
        //validateBookNotOnLoan(loanDTO.getBook().getId());

        Long bookId = loanDTO.getBook().getId();
        Long userId = loanDTO.getUser().getId();

        BookDAO book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("[Service] No book found with id {}", bookId);
                    return new BookException(new ExceptionEntity(404, "Book not found with id: " + bookId));
                });

        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("[Service] No user found with id {}", userId);
                    return new UserException(new ExceptionEntity(404, "User not found with id: " + userId));
                });

        LoanDAO loanDAO = loanMapper.loanDTOToLoanDAO(loanDTO);
        loanDAO.setBook(book);
        loanDAO.setUser(user);

        LoanDAO createdLoanDAO = loanRepository.save(loanDAO);
        LoanDTO createdLoanDTO = loanMapper.loanDAOToLoanDTO(createdLoanDAO);

        logger.info("[Service] Loan created with user {} and book {}", user.getName(), book.getTitle());

        return createdLoanDTO;
    }

    @Override
    public LoanDTO updateLoan(Long id, LoanDTO loanDTO) {
        logger.info("[Service] Updating loan with id {}", id);

        validateLoan(loanDTO);

        LoanDAO loanDAO = loanRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("[Service] No loan found with id {}", id);
                    return new LoanException(new ExceptionEntity(404, "Loan not found with id: " + id));
                });

        Long bookId = loanDTO.getBook().getId();
        Long userId = loanDTO.getUser().getId();

        BookDAO book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("[Service] No book found with id {}", bookId);
                    return new BookException(new ExceptionEntity(404, "Book not found with id: " + bookId));
                });

        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("[Service] No user found with id {}", userId);
                    return new UserException(new ExceptionEntity(404, "User not found with id: " + userId));
                });

        loanDAO.setUser(user);
        loanDAO.setBook(book);
        loanDAO.setLoanDate(loanDTO.getLoanDate());
        loanDAO.setReturnDate(loanDTO.getReturnDate());

        LoanDAO updatedLoanDAO = loanRepository.save(loanDAO);
        LoanDTO updatedLoanDTO = loanMapper.loanDAOToLoanDTO(updatedLoanDAO);

        logger.info("[Service] Loan with id {} updated successfully", id);

        return updatedLoanDTO;
    }

    @Override
    public LoanDTO partiallyUpdateLoan(Long id, Map<String, Object> updates) {
        logger.debug("LoanServiceImpl: Partially updating loan with id {}...", id);
        ExceptionEntity err = new ExceptionEntity(404, "Loan not found with id: " + id);
        LoanDAO loanDAO = loanRepository.findById(id).orElseThrow(() -> new LoanException(err));

        if (updates.containsKey("loanDate")) {
            loanDAO.setLoanDate((LocalDate) updates.get("loanDate"));
        }
        if (updates.containsKey("returnDate")) {
            loanDAO.setReturnDate((LocalDate) updates.get("returnDate"));
        }
        if (updates.containsKey("bookId")) {
            Long bookId = ((Number) updates.get("bookId")).longValue();
            BookDAO bookDAO = bookRepository.findById(bookId)
                    .orElseThrow(() -> new LoanException(new ExceptionEntity(404, "Book not found with id: " + bookId)));
            loanDAO.setBook(bookDAO);
        }

        if (updates.containsKey("userId")) {
            Long userId = ((Number) updates.get("userId")).longValue();
            UserDAO userDAO = userRepository.findById(userId)
                    .orElseThrow(() -> new LoanException(new ExceptionEntity(404, "User not found with id: " + userId)));
            loanDAO.setUser(userDAO);
        }

        LoanDAO updatedLoanDAO = loanRepository.save(loanDAO);
        LoanDTO updatedLoanDTO = loanMapper.loanDAOToLoanDTO(updatedLoanDAO);

        logger.debug("LoanServiceImpl: partiallyUpdateLoan() -> Loan updated: {}", updatedLoanDTO);
        return updatedLoanDTO;
    }

    @Override
    public void deleteLoan(Long id) {
        logger.debug("LoanServiceImpl: Deleting loan with id {}...", id);
        if (!loanRepository.existsById(id)) {
            throw new LoanException(new ExceptionEntity(404, "Loan not found with id: " + id));
        }
        loanRepository.deleteById(id);
        logger.debug("LoanServiceImpl: deleteLoan() -> Loan deleted with id: {}", id);
    }
}