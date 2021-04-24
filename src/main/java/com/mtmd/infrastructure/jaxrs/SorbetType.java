package com.mtmd.infrastructure.jaxrs;

import java.util.List;

public class SorbetType extends CategoryType {
    public int fruitContentInPercent;
    public List<String> fruits;

    @Override
    public String getName() {
        return "Sorbet";
    }
}
