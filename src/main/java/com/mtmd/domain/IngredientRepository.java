package com.mtmd.domain;

import java.util.Optional;

public interface IngredientRepository {
    Ingredient add(Ingredient ingredient);

    Optional<Ingredient> findById(String name);
}
