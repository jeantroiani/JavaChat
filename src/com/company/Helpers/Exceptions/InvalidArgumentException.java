package com.company.Helpers.Exceptions;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String message, Throwable err) {
        super(message, err);
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
