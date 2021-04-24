package com.mtmd.domain.category;

import com.mtmd.domain.Ice;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "category_sorbet")
@DiscriminatorValue("S")
public final class Sorbet extends Category {

    @Column(name="fruitcontentinpercent")
    Integer fruitContentInPercent;
    @ElementCollection
    @Column(name = "fruit")
    @CollectionTable(name="sorbet_fruits", joinColumns = {@JoinColumn(name="sorbet_id")})
    List<String> fruits;

    protected Sorbet() {}

    public Sorbet(int fruitContentInPercent, List<String> fruits){
        Objects.requireNonNull(fruits, "Fruits must be defined!");
        this.fruitContentInPercent = fruitContentInPercent;
        this.fruits = fruits;
    }

    public Integer getFruitContentInPercent() {
        return fruitContentInPercent;
    }

    public List<String> getFruits() {
        return Collections.unmodifiableList(fruits);
    }
}
