package com.mtmd.domain;

import com.mtmd.domain.category.Category;
import com.mtmd.infrastructure.persistence.JpaMoneyConverter;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
public class Ice extends BaseEntity
{
    @Id
    @Column(unique=true)
    @NotNull
    String name;
    @OneToOne
    @JoinColumn(name="category_id")
    Category category;
    @NotNull
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(
            name="ice_ingredients",
            joinColumns = {@JoinColumn(name="ice_id")},
            inverseJoinColumns = {@JoinColumn(name="ingredient_id")})
    Set<Ingredient> ingredients = new HashSet<>(10);
    int nutrients;
    @NotNull
    @Convert(converter = JpaMoneyConverter.class)
    Money purchasePrice;
    @NotNull
    @Convert(converter = JpaMoneyConverter.class)
    Money retailPrice;
    String foodIntolerances;

    // Needed only for JPA
    protected Ice() {}

    public Ice(String name, Category category, Set<Ingredient> ingredients, int nutrients, Money purchasePrice, Money retailPrice, Optional<String> foodIntolerances) {
        Objects.requireNonNull(name, "Name must be defined!");
        Objects.requireNonNull(category, "Category must be defined!");
        Objects.requireNonNull(ingredients, "Ingredients must be defined!");
        if(ingredients.isEmpty()){
            throw new NullPointerException("Ingredients must not be empty!");
        }
        Objects.requireNonNull(purchasePrice, "PurchasePrice must be defined!");
        Objects.requireNonNull(retailPrice, "RetailPrice must be defined!");
        this.name = name.replace(" ", "-");
        this.category = category;
        for (Ingredient ingredient : ingredients) {
            this.addIngredient(ingredient);
        }
        this.nutrients = nutrients;
        this.purchasePrice = purchasePrice;
        this.retailPrice = retailPrice;
        foodIntolerances.ifPresent(s -> this.foodIntolerances = s);
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        ingredient.addIce(this);
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getNutrients() {
        return nutrients;
    }

    public Money getPurchasePrice() {
        return purchasePrice;
    }

    public Money getRetailPrice() {
        return retailPrice;
    }

    public Optional<String> getFoodIntolerances() {
        return Optional.ofNullable(foodIntolerances);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ice ice = (Ice) o;
        return name.equals(ice.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Ice{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", ingredients=" + ingredients +
                ", nutrients=" + nutrients +
                ", purchasePrice=" + purchasePrice +
                ", retailPrice=" + retailPrice +
                ", foodIntolerances=" + foodIntolerances +
                '}';
    }

    //FIXME this is completely bogus and needed due to transient properties -> improve mapping
    public void setCategory(Category category) {
        this.category = category;
    }
}
