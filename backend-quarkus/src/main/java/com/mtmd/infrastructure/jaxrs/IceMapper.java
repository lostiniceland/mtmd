package com.mtmd.infrastructure.jaxrs;

import com.mtmd.application.ValidationException;
import com.mtmd.domain.Ice;
import com.mtmd.domain.Ingredient;
import com.mtmd.domain.category.Category;
import com.mtmd.domain.category.Cream;
import com.mtmd.domain.category.Sorbet;
import com.mtmd.domain.category.Water;
import org.javamoney.moneta.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface IceMapper {

    static final Pattern moneyPattern = Pattern.compile("\\d+(\\.\\d{1,2})? EUR");

    default List<com.mtmd.infrastructure.jaxrs.gen.types.Ice> toResources(List<Ice> list){
        return list.stream().map(ice -> {
            com.mtmd.infrastructure.jaxrs.gen.types.Ice result;
            if(ice.getCategory() instanceof Sorbet){
                result = toSorbetResource(ice);
            }else if(ice.getCategory() instanceof Water){
                result = toWaterResource(ice);
            }else if(ice.getCategory() instanceof Cream){
                result = toCreamResource(ice);
            }else{
                throw new RuntimeException("Unmapped");
            }
            return result;
        }).collect(Collectors.toList());
    }

    default String toResource (Ingredient ingredient){
        return ingredient.getName();
    }
    default Optional<List<String>> map(List<String> value) {
        return Optional.ofNullable(value);
    }

    default com.mtmd.infrastructure.jaxrs.gen.types.Ice toResource(com.mtmd.domain.Ice ice) {
        com.mtmd.infrastructure.jaxrs.gen.types.Ice result;
        if(ice.getCategory() instanceof Sorbet){
            result = toSorbetResource(ice);
        }else if(ice.getCategory() instanceof Water){
            result = toWaterResource(ice);
        }else if(ice.getCategory() instanceof Cream){
            result = toCreamResource(ice);
        }else{
            throw new RuntimeException("Unmapped");
        }
        return result;
    }

    @Mapping(target = "category", constant = "Water")
    @Mapping(target = "flavourAdditive", expression = "java(((com.mtmd.domain.category.Water)ice.getCategory()).getFlavourAdditive())")
    com.mtmd.infrastructure.jaxrs.gen.types.Water toWaterResource(Ice ice);

    @Mapping(target = "category", constant = "Sorbet")
    @Mapping(target = "fruitContentInPercent", expression = "java(((com.mtmd.domain.category.Sorbet)ice.getCategory()).getFruitContentInPercent())")
    @Mapping(target = "fruits", expression = "java(((com.mtmd.domain.category.Sorbet)ice.getCategory()).getFruits())")
    com.mtmd.infrastructure.jaxrs.gen.types.Sorbet toSorbetResource(Ice ice);

    @Mapping(target = "category", constant = "Cream")
    @Mapping(target = "creamInPercent", expression = "java(((com.mtmd.domain.category.Cream)ice.getCategory()).getCreamInPercent())")
    com.mtmd.infrastructure.jaxrs.gen.types.Cream toCreamResource(Ice ice);

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
        if(!moneyPattern.matcher(value).matches()){
            throw new ValidationException("Money must conform to '1.00 EUR' or '1 EUR'");
        }
        String[] split = value.split(" ");
        return Money.of(new BigDecimal(split[0]), Monetary.getCurrency(split[1]));
    }

    default String mapOptionalString(Optional<String> value){
        return value.orElse(null);
    }

    default Category toCategory(com.mtmd.infrastructure.jaxrs.gen.types.Ice ice){
        Category category;
        if (ice instanceof com.mtmd.infrastructure.jaxrs.gen.types.Water){
            List<String> flavourAdditive = ((com.mtmd.infrastructure.jaxrs.gen.types.Water) ice).getFlavourAdditive();
            category = new Water(Optional.of(flavourAdditive));
        }else if(ice instanceof com.mtmd.infrastructure.jaxrs.gen.types.Sorbet){
            com.mtmd.infrastructure.jaxrs.gen.types.Sorbet sorbet = (com.mtmd.infrastructure.jaxrs.gen.types.Sorbet) ice;
            category = new Sorbet(sorbet.getFruitContentInPercent(), sorbet.getFruits());
        }else if (ice instanceof com.mtmd.infrastructure.jaxrs.gen.types.Cream){
            category = new Cream(((com.mtmd.infrastructure.jaxrs.gen.types.Cream) ice).getCreamInPercent());
        }else if(ice.getCategory().equals("Water")) {
            category = new Water(Optional.empty());
        }else if(ice.getCategory().equals("Sorbet")) {
            category = new Sorbet(0, Collections.emptyList());
        }else if(ice.getCategory().equals("Cream")) {
            category = new Cream(0);
        }else {
            throw new IllegalArgumentException("Unsupported Ice-Category");
        }
        return category;
    }
}
