package com.mtmd.domain;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Ingredient extends BaseEntity {

    @Id
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Ice> usingIces = new HashSet<>();

    // Needed only for JPA
    protected Ingredient() {}

    public Ingredient(String name){
        Objects.requireNonNull(name, "Name must be defined!");
        this.name = name;
    }

    /** Only call via Ice#addIgredient **/
    void addIce(Ice ice){
        this.usingIces.add(ice);
    }

    public String getName() {
        return name;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                '}';
    }
}
