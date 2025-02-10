package com.example.demo.loan.controller;

import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.exception.LoanException;
import com.example.demo.loan.service.LoanService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;

    @GetMapping
    List<LoanDTO> getLoans() {
        logger.info("Getting all loans...");
        List<LoanDTO> listOfLoans = loanService.getLoans();
        logger.info("LoanController: getLoans() -> {} loans obtained.", listOfLoans);
        return listOfLoans;
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getLoanById(@PathVariable Long id) {
        logger.info("Getting loan with id {}...", id);
        try {
            LoanDTO loan = loanService.getLoanById(id);
            logger.info("LoanController: getLoanById() -> Loan obtained: {}", loan);
            return ResponseEntity.ok(loan);
        } catch (LoanException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PostMapping
    LoanDTO createLoan(@RequestBody LoanDTO loanDTO) {
        logger.info("Creating a new loan...");
        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        logger.info("LoanController: createLoan() -> Loan created: {}", createdLoan);
        return createdLoan;
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> updateLoan(@PathVariable Long id, @RequestBody LoanDTO loanDTO) {
        logger.info("Updating loan with id {}...", id);
        try {
            LoanDTO updatedLoan = loanService.updateLoan(id, loanDTO);
            logger.info("LoanController: updateLoan() -> Loan updated: {}", updatedLoan);
            return ResponseEntity.ok(updatedLoan);
        } catch (LoanException e) {
            logger.error("LoanController: Error updating loan with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<Object> partiallyUpdateLoan(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        logger.info("Partially updating loan with id {}...", id);
        try {
            LoanDTO updatedLoan = loanService.partiallyUpdateLoan(id, updates);
            logger.info("LoanController: partiallyUpdateLoan() -> Loan updated: {}", updatedLoan);
            return ResponseEntity.ok(updatedLoan);
        } catch (LoanException e) {
            logger.error("LoanController: Error partially updating loan with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteLoan(@PathVariable Long id) {
        logger.info("Deleting loan with id {}...", id);
        try {
            loanService.deleteLoan(id);
            logger.info("LoanController: deleteLoan() -> Loan deleted with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (LoanException e) {
            logger.error("LoanController: Error deleting loan with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErr());
        }
    }
}