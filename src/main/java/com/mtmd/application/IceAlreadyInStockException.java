package com.mtmd.application;

public class IceAlreadyInStockException extends RuntimeException {
    public IceAlreadyInStockException(String message){
        super(message);
    }
}
