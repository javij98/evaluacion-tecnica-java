package com.example.demo.loan.dto;

import com.example.demo.book.dto.BookDTO;
import com.example.demo.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class LoanDTO {
    private Long id;
    private BookDTO book;
    private UserDTO user;
    private LocalDate loanDate;
    private LocalDate returnDate;
}