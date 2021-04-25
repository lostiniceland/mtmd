package com.mtmd.infrastructure.jaxrs;

import com.mtmd.domain.Ice;
import com.mtmd.domain.Ingredient;
import com.mtmd.domain.category.Category;
import com.mtmd.domain.category.Cream;
import com.mtmd.domain.category.Sorbet;
import com.mtmd.domain.category.Water;
import com.mtmd.infrastructure.jaxrs.gen.types.*;
import org.javamoney.moneta.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface IceMapper {

    IceType toResource(Ice ice);
    List<IceType> toResources(List<Ice> list);

    default String toResource (Ingredient ingredient){
        return ingredient.getName();
    }
    default Optional<List<String>> map(List<String> value) {
        return Optional.ofNullable(value);
    }

    default CategoryType toResource(Category category) {
        CategoryType result;
        if(category instanceof Sorbet){
            result = toResource((Sorbet)category);
        }else if(category instanceof Water){
            result = toResource((Water)category);
        }else if(category instanceof Cream){
            result = toResource((Cream)category);
        }else{
            throw new RuntimeException("Unmapped");
        }
        return result;
    }
//    CategoryType toResource(Category category);
    @Mapping(target = "name", constant = "SorbetType")
    SorbetType toResource(Sorbet category);
    @Mapping(target = "name", constant = "WaterType")
    WaterType toResource(Water category);
    @Mapping(target = "name", constant = "CreamType")
    CreamType toResource(Cream category);

    default Category toCategory(CategoryType categoryType){
        Category result;
        if(categoryType instanceof SorbetType){
            result = toCategory((SorbetType)categoryType);
        }else if(categoryType instanceof WaterType){
            result = toCategory((WaterType)categoryType);
        }else if(categoryType instanceof CreamType){
            result = toCategory((CreamType)categoryType);
        }else{
            throw new RuntimeException("Unmapped");
        }
        return result;
    }
//    @Mapping(source = "name", target = "", ignore = true)
//    Category toCategory(CategoryType category);
    Sorbet toCategory(SorbetType category);
    Water toCategory(WaterType category);
    Cream toCategory(CreamType category);

    default String mapMoneyToString(Money money){
        if(money == null){
            return null;
        }
        return String.format("%s %s", money.getNumber(), money.getCurrency().getCurrencyCode());
    }

    default Money mapStringToMoney(String value){
        if(value == null){
            return null;
        }
        String[] split = value.split(" ");
        return Money.of(new BigDecimal(split[0]), Monetary.getCurrency(split[1]));
    }

    default String mapOptionalString(Optional<String> value){
        return value.orElse(null);
    }
}
