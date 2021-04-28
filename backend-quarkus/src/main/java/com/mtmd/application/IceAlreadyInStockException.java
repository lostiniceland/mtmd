package com.mtmd.application;

public class IceAlreadyInStockException extends DomainException {
    public IceAlreadyInStockException(String message){
        super(message);
    }
}
