package com.mtmd.application;

public class IceNotFoundException extends RuntimeException {
    public IceNotFoundException(String message){
        super(message);
    }
}
