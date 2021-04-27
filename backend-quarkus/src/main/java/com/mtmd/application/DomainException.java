package com.mtmd.application;

public class DomainException extends RuntimeException {
    DomainException(String message){
        super(message);
    }
    DomainException(String message, Throwable cause){
        super(message, cause);
    }
}
