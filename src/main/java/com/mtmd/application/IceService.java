package com.mtmd.application;

import com.mtmd.domain.Ice;
import com.mtmd.domain.IceRepository;
import com.mtmd.domain.Ingredient;
import com.mtmd.domain.IngredientRepository;
import com.mtmd.domain.category.Category;
import org.javamoney.moneta.Money;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class IceService {

    @Inject
    IceRepository iceRepository;
    @Inject
    IngredientRepository ingredientRepository;

    public List<Ice> loadAllIceCreams(){
        return iceRepository.findAll();
    }

    public Ice loadIce(String name){
        return iceRepository.findById(name)
                .orElseThrow(() -> new IceNotFoundException(String.format("No ice with name '%s' found!", name)));
    }

    @Transactional
    public Ice addIce(String name, Category category, Collection<String> ingredients, int nutrients, Money purchasePrice, Money retailPrice, Optional<String> foodIntolerances){

        if(iceRepository.findById(name).isPresent()){
            throw new IceAlreadyInStockException(String.format("Ice with name '%s' is already in stock!", name));
        }
        Set<Ingredient> ingredientEntities = ingredients.stream().map(s -> ingredientRepository.findById(s)
                .orElseGet(() -> ingredientRepository.add(new Ingredient(s)))) // use lambda for lazy evaluation
                .collect(Collectors.toSet());
        return iceRepository.add(new Ice(name, category, ingredientEntities, nutrients, purchasePrice, retailPrice, foodIntolerances));
    }

}
