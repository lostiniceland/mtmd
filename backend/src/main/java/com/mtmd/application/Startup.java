package com.mtmd.application;

import com.mtmd.domain.category.Cream;
import com.mtmd.domain.category.Sorbet;
import com.mtmd.domain.category.Water;
import io.quarkus.runtime.StartupEvent;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.money.Monetary;
import javax.transaction.Transactional;
import java.util.*;


@ApplicationScoped
public class Startup {

    private static final Logger logger = LoggerFactory.getLogger(Startup.class);

    @Inject
    IceService service;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        try {
            // Vanilla
            Set<String> ingredients = new HashSet<>(3);
            ingredients.add("Milk");
            service.addIce("Vanilla",
                    new Cream(10),
                    ingredients,
                    300,
                    Money.of(1, Monetary.getCurrency("EUR")),
                    Money.of(1.5, Monetary.getCurrency("EUR")),
                    Optional.empty());
            // Orange
            List<String> flavourAdditive = new ArrayList<>(2);
            flavourAdditive = new ArrayList<>(2);
            flavourAdditive.add("additive 1");
            flavourAdditive.add("additive 2");
            ingredients = new HashSet<>(3);
            ingredients.add("Orange");
            service.addIce(
                    "Orange",
                    new Water(Optional.of(flavourAdditive)),
                    ingredients,
                    150,
                    Money.of(0.5, Monetary.getCurrency("EUR")),
                    Money.of(1.0, Monetary.getCurrency("EUR")),
                    Optional.of("allergy"));
            // Strawberry-Swirl
            List<String> fruits = new ArrayList<>(2);
            fruits.add("Strawberry");
            fruits.add("Aple");
            ingredients = new HashSet<>(3);
            ingredients.add("Water");
            ingredients.add("Strawberry");
            ingredients.add("Apple");
            service.addIce(
                    "Strawberry-Swirl",
                    new Sorbet(50, fruits),
                    ingredients,
                    150,
                    Money.of(1, Monetary.getCurrency("EUR")),
                    Money.of(3, Monetary.getCurrency("EUR")),
                    Optional.of("Strawberry Allergy"));
        }catch(IceAlreadyInStockException e){
            logger.info("Demo Content already loaded");
        }catch(Throwable e){
            logger.error("Error during storing demo content");
            // we want to cancel the deployment because something is completely off
            if( e instanceof TechnicalException ){
                // but we dont want to wrap it twice
                throw e;
            }else {
                throw new TechnicalException(e);
            }
        }
    }
}
