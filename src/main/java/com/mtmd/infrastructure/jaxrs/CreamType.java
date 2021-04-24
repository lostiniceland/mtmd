package com.mtmd.infrastructure.jaxrs;

public class CreamType extends CategoryType {
    public  int creamInPercent;

    @Override
    public String getName() {
        return "Cream";
    }
}
