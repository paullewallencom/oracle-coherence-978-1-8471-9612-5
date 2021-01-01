package com.seovic.samples.bank.domain;


import java.math.BigDecimal;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public interface ExchangeRateRepository
    {
    BigDecimal getExchangeRate(String instrument);
    }
