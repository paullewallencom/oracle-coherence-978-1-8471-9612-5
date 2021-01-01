package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.ExchangeRateRepository;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class FakeExchangeRateRepository implements ExchangeRateRepository
    {
    public FakeExchangeRateRepository()
        {
        /*
        EUR/USD  	1.5083
        USD/EUR  	0.6630
        GBP/USD 	1.6616
        USD/GBP 	0.6017
        AUD/USD 	0.9262
        USD/AUD 	1.0814
        CAD/USD 	0.9554
        USD/CAD 	1.0436
        CHF/USD 	1.0006
        USD/CHF 	0.9994
        JPY/USD 	0.0115
        USD/JPY 	86.7050
        */
        m_exchangeRates = new HashMap<String, BigDecimal>();
        m_exchangeRates.put("EURUSD", new BigDecimal("1.5083"));
        m_exchangeRates.put("USDEUR", new BigDecimal("0.6630"));
        m_exchangeRates.put("GBPUSD", new BigDecimal("1.6616"));
        m_exchangeRates.put("USDGBP", new BigDecimal("0.6017"));
        m_exchangeRates.put("AUDUSD", new BigDecimal("0.9262"));
        m_exchangeRates.put("USDAUD", new BigDecimal("1.0814"));
        m_exchangeRates.put("CADUSD", new BigDecimal("0.9554"));
        m_exchangeRates.put("USDCAD", new BigDecimal("1.0436"));
        m_exchangeRates.put("CHFUSD", new BigDecimal("1.0006"));
        m_exchangeRates.put("USDCHF", new BigDecimal("0.9994"));
        m_exchangeRates.put("JPYUSD", new BigDecimal("0.0115"));
        m_exchangeRates.put("USDJPY", new BigDecimal("86.7050"));
        }

    public BigDecimal getExchangeRate(String instrument)
        {
        return m_exchangeRates.get(instrument);
        }

    private Map<String, BigDecimal> m_exchangeRates;
    }
