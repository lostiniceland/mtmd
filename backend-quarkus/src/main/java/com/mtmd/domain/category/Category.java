package com.mtmd.domain.category;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="category")
@DiscriminatorColumn(name="category_type")
public abstract class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_SEQ")
    @SequenceGenerator(name = "CATEGORY_SEQ", sequenceName = "category_seq")
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public abstract String getName();
}

