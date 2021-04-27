package com.mtmd.infrastructure.jaxrs;

import com.mtmd.application.DomainException;
import com.mtmd.application.IceNotFoundException;
import com.mtmd.application.TechnicalException;
import com.mtmd.application.ValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        Response response;
        if(exception instanceof TechnicalException){
            // no, we dont send technical error information to our client
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }else if(exception instanceof DomainException || exception instanceof ValidationException){
            // in case of domain-logic, the client needs to know
            if(exception instanceof IceNotFoundException){
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity(formatJsonResponseMessage(exception.getMessage())).build();
            }else {
                response = Response.status(Response.Status.BAD_REQUEST)
                        .entity(formatJsonResponseMessage(exception.getMessage())).build();
            }
        }else{
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private String formatJsonResponseMessage(String text){
        return String.format("{\"error\": \"%s\"}",text);
    }
}