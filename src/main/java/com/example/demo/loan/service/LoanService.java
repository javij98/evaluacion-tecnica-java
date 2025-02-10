package com.example.demo.loan.service;

import com.example.demo.loan.dto.LoanDTO;

import java.util.List;
import java.util.Map;

public interface LoanService {
    List<LoanDTO> getLoans();
    LoanDTO getLoanById(Long id);
    LoanDTO createLoan(LoanDTO loanDTO);
    LoanDTO updateLoan(Long id, LoanDTO loanDTO);
    LoanDTO partiallyUpdateLoan(Long id, Map<String, Object> updates);
    void deleteLoan(Long id);
    //List<LoanDTO> getLoansByBookId(Long bookId);
    //List<LoanDTO> getLoansByUserId(Long userId);
}