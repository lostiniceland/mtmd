package com.mtmd.domain.category;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "category_water")
@DiscriminatorValue("W")
public final class Water extends Category {

    @ElementCollection
    @Column(name = "flavour_additive")
    @CollectionTable(name="water_flavour_additives", joinColumns = {@JoinColumn(name="water_id")})
    List<String> flavourAdditive;

    protected Water() {}

    public Water(Optional<List<String>> flavourAdditive){
        Objects.requireNonNull(flavourAdditive, "FlavourAdditive must be defined!");
        flavourAdditive.ifPresent(list -> this.flavourAdditive = list);
    }

    public List<String> getFlavourAdditive(){
        if(flavourAdditive == null){
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(flavourAdditive);
    }

    @Override
    public String getName() {
        return "Water";
    }
}
