package com.mtmd.infrastructure.persistence;

import com.mtmd.domain.Ingredient;
import com.mtmd.domain.IngredientRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@ApplicationScoped
public class JpaIngredientRepository implements IngredientRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Ingredient add(Ingredient ingredient) {
        em.persist(ingredient);
        return ingredient;
    }

    @Override
    public Optional<Ingredient> findById(String name) {
        return Optional.ofNullable(em.find(Ingredient.class, name));
    }
}
