package com.mtmd.application;

import javax.enterprise.inject.Vetoed;

public class TechnicalException extends RuntimeException {
    public TechnicalException(Throwable cause){
        super(cause);
    }
}
