package com.mtmd.infrastructure.persistence

import org.javamoney.moneta.Money;
import spock.lang.Specification
import spock.lang.Unroll

import javax.money.Monetary;

class JpaMoneyConverterTest extends Specification {

    def sut = new JpaMoneyConverter();

    @Unroll
    def "Money type with #moneyAmount is properly converted to '#moneyAmount|EUR'" (){
        given:
        def amount = Money.of(moneyAmount, Monetary.getCurrency("EUR"))

        expect:
        sut.convertToDatabaseColumn(amount) == expectedString

        where:
        moneyAmount | expectedString
        100         | "100|EUR"
        10.25       | "10.25|EUR"

    }
}
