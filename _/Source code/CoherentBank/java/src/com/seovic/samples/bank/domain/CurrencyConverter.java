package com.seovic.samples.bank.domain;


import java.util.Currency;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public interface CurrencyConverter
    {
    CurrencyConversion convert(Money money, Currency targetCurrency);
    }
