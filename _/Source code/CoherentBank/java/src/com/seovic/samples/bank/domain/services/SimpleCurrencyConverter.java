package com.seovic.samples.bank.domain.services;


import com.seovic.samples.bank.domain.CurrencyConverter;
import com.seovic.samples.bank.domain.CurrencyConversion;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.ExchangeRateRepository;
import com.seovic.samples.bank.domain.RepositoryRegistry;
import com.seovic.samples.bank.domain.Currencies;
import com.seovic.samples.bank.domain.UnsupportedCurrencyException;

import java.util.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class SimpleCurrencyConverter implements CurrencyConverter
    {
    // ---- constructors ----------------------------------------------------

    public SimpleCurrencyConverter()
        {
        }

    public SimpleCurrencyConverter(ExchangeRateRepository exchangeRateRepository)
        {
        m_exchangeRateRepository = exchangeRateRepository;
        }


    // ---- CurrencyConverter implementation --------------------------------

    public CurrencyConversion convert(Money amount, Currency targetCurrency)
        {
        Money defaultCurrencyAmount = convertToDefaultCurrency(amount);
        Money targetCurrencyAmount  = DEFAULT_CURRENCY.equals(targetCurrency)
                                       ? defaultCurrencyAmount
                                       : convertToTargetCurrency(defaultCurrencyAmount, targetCurrency);

        return new CurrencyConversion(amount, targetCurrencyAmount);
        }


    // ---- helper methods --------------------------------------------------

    protected Money convertToDefaultCurrency(Money amount)
        {
        return DEFAULT_CURRENCY.equals(amount.getCurrency())
                ? amount
                : convertToTargetCurrency(amount, DEFAULT_CURRENCY);
        }

    protected Money convertToTargetCurrency(Money amount, Currency targetCurrency)
        {
        String     instrument   = amount.getCurrency().getCurrencyCode() + targetCurrency.getCurrencyCode();
        BigDecimal exchangeRate = getExchangeRateRepository().getExchangeRate(instrument);
        if (exchangeRate == null)
            {
            throw new UnsupportedCurrencyException(amount.getCurrency());
            }
        BigDecimal convertedAmount = amount.getAmount().multiply(exchangeRate);
        convertedAmount = convertedAmount.setScale(targetCurrency.getDefaultFractionDigits(),
                                                   RoundingMode.HALF_EVEN);
        return new Money(convertedAmount, targetCurrency);
        }


    // ---- dependencies ----------------------------------------------------

    /**
     * Exchange rate repository.
     */
    private transient ExchangeRateRepository m_exchangeRateRepository;
    protected ExchangeRateRepository getExchangeRateRepository()
        {
        if (m_exchangeRateRepository == null)
            {
            m_exchangeRateRepository = RepositoryRegistry.getExchangeRateRepository();
            }
        return m_exchangeRateRepository;
        }

    public void setExchangeRateRepository(ExchangeRateRepository exchangeRateRepository)
        {
        m_exchangeRateRepository = exchangeRateRepository;
        }


    // ---- static members --------------------------------------------------

    private static final Currency DEFAULT_CURRENCY = Currencies.USD;
    }
