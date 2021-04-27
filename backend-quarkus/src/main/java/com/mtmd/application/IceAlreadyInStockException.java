package com.mtmd.application;

public class IceAlreadyInStockException extends DomainException {
    IceAlreadyInStockException(String message){
        super(message);
    }
}
