package com.mtmd.infrastructure.persistence;

import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/**
 * Converts Java Money type to DB and back again by using a simple custom format "amount|currency"
 */
@Converter(autoApply = true)
public class JpaMoneyConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money money) {
        return String.format("%s|%s", money.getNumber().toString(), money.getCurrency().getCurrencyCode());
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        String[] split = dbData.split("\\|");
        return Money.of(new BigDecimal(split[0]), Monetary.getCurrency(split[1]));
    }
}
