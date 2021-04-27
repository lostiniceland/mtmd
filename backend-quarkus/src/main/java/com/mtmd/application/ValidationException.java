package com.mtmd.application;

public class ValidationException extends RuntimeException {
    ValidationException(String message, Throwable cause){
        super(message, cause);
    }
}
