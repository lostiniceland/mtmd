package com.mtmd.domain.category;

import com.mtmd.domain.Ice;

import javax.persistence.*;

@Entity
@Table(name = "category_cream")
@DiscriminatorValue("C")
public final class Cream extends Category {

    int creamInPercent;

    protected Cream() {}

    public Cream(int creamInPercent){
        this.creamInPercent = creamInPercent;
    }

    public int getCreamInPercent() {
        return creamInPercent;
    }
}
