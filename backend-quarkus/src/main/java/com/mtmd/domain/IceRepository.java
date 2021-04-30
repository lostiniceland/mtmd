package com.mtmd.domain;

import java.util.List;
import java.util.Optional;

public interface IceRepository {
    Ice add(Ice ice);

    Optional<Ice> findById(String vanilla);

    List<Ice> findAll();

    Optional<Ingredient> findIngredientById(String name);
}
