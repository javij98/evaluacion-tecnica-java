package com.example.demo.book.exception;

import com.example.demo.exceptions.models.ExceptionEntity;
import lombok.Getter;

@Getter
public class BookException extends RuntimeException {

    private final ExceptionEntity err;

    public BookException(ExceptionEntity err) {
        super(String.format("[%d]: %s", err.getCode(), err.getMessage()));
        this.err = err;
    }
}