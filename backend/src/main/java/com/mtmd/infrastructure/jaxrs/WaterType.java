package com.mtmd.infrastructure.jaxrs;

import java.util.List;

public class WaterType extends CategoryType{
    public List<String> flavourAdditive;

    @Override
    public String getName() {
        return "Water";
    }
}
