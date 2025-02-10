package com.example.demo;

import com.example.demo.book.dao.BookDAO;
import com.example.demo.book.repository.BookRepository;
import com.example.demo.loan.dao.LoanDAO;
import com.example.demo.loan.repository.LoanRepository;
import com.example.demo.user.dao.UserDAO;
import com.example.demo.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Dataloader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public Dataloader(UserRepository userRepository, BookRepository bookRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;

        this.loanRepository = loanRepository;
    }

    @Override
    public void run(String... args) {

        // Create 10 users
        for (int i = 1; i <= 10; i++) {
            UserDAO userDAO = new UserDAO();
            userDAO.setName("User " + i);
            userDAO.setPhoneNumber("123456789" + i);
            userDAO.setRegistrationDate(LocalDate.now());
            userRepository.save(userDAO);
        }

        // Create 10 books
        for (int i = 1; i <= 10; i++) {
            BookDAO bookDAO = new BookDAO();
            bookDAO.setTitle("Book " + i);
            bookDAO.setAuthor("Author " + i);
            bookDAO.setIsbn("ISBN " + i);
            bookDAO.setPublicationDate(LocalDate.now());
            bookRepository.save(bookDAO);
        }

        // Create 10 loans
        for (int i = 1; i <= 10; i++) {
            LoanDAO loanDAO = new LoanDAO();
            loanDAO.setBook(bookRepository.findById((long) i).orElse(null));
            loanDAO.setUser(userRepository.findById((long) i).orElse(null));
            loanDAO.setLoanDate(LocalDate.now());
            loanDAO.setReturnDate(LocalDate.now().plusDays(30));
            loanRepository.save(loanDAO);
        }
    }
}