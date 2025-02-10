package com.example.demo.user.exception;

import com.example.demo.exceptions.models.ExceptionEntity;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ExceptionEntity err;

    public UserException(ExceptionEntity err) {
        super(String.format("[%d]: %s", err.getCode(), err.getMessage()));
        this.err = err;
    }
}
