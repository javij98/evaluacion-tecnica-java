package com.example.demo.loan.exception;

import com.example.demo.exceptions.models.ExceptionEntity;
import lombok.Getter;

@Getter
public class LoanException extends RuntimeException {

    private final ExceptionEntity err;

    public LoanException(ExceptionEntity err) {
        super(String.format("[%d]: %s", err.getCode(), err.getMessage()));
        this.err = err;
    }
}