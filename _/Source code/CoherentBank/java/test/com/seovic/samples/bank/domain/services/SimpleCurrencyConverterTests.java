package com.seovic.samples.bank.domain.services;


import static org.junit.Assert.*;
import org.junit.Test;

import static com.seovic.samples.bank.domain.Currencies.*;

import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.CurrencyConversion;
import com.seovic.samples.bank.domain.repository.FakeExchangeRateRepository;

import java.math.BigDecimal;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class SimpleCurrencyConverterTests
    {
    @Test
    public void testUSDtoEUR()
        {
        Money amount = new Money("100", USD);

        CurrencyConversion conversion = converter.convert(amount, EUR);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("66.30", EUR), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("0.6630"), conversion.getExchangeRate());
        }

    @Test
    public void testEURtoUSD()
        {
        Money amount = new Money("100", EUR);

        CurrencyConversion conversion = converter.convert(amount, USD);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("150.83", USD), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("1.5083"), conversion.getExchangeRate());
        }

    @Test
    public void testUSDtoJPY()
        {
        Money amount = new Money("100", USD);

        CurrencyConversion conversion = converter.convert(amount, JPY);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("8670", JPY), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("86.7000"), conversion.getExchangeRate());
        }

    @Test
    public void testJPYtoUSD()
        {
        Money amount = new Money("100", JPY);

        CurrencyConversion conversion = converter.convert(amount, USD);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("1.15", USD), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("0.0115"), conversion.getExchangeRate());
        }

    @Test
    public void testEURtoJPY()
        {
        Money amount = new Money("100", EUR);

        CurrencyConversion conversion = converter.convert(amount, JPY);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("13078", JPY), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("130.7800"), conversion.getExchangeRate());
        }

    @Test
    public void testJPYtoEUR()
        {
        Money amount = new Money("100", JPY);

        CurrencyConversion conversion = converter.convert(amount, EUR);

        assertEquals(amount, conversion.getOriginalAmount());
        assertEquals(new Money("0.76", EUR), conversion.getConvertedAmount());
        assertEquals(new BigDecimal("0.0076"), conversion.getExchangeRate());
        }
    
    private SimpleCurrencyConverter converter =
            new SimpleCurrencyConverter(new FakeExchangeRateRepository());
    }
