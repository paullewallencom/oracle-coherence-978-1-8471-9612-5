package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.ExchangeRateRepository;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.math.BigDecimal;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class CoherenceExchangeRateRepository
        implements ExchangeRateRepository
    {
    public BigDecimal getExchangeRate(String instrument)
        {
        return (BigDecimal) m_exchangeRates.get(instrument);
        }

    private static final NamedCache m_exchangeRates =
            CacheFactory.getCache("exchange-rates");
    }