package com.seovic.samples.bank.domain;


import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class CurrencyConversion
    {
    // ---- constructors ----------------------------------------------------

    public CurrencyConversion(Money originalAmount,
                              Money convertedAmount)
        {
        m_originalAmount = originalAmount;
        m_convertedAmount = convertedAmount;
        }


    // ---- properties ------------------------------------------------------

    public Money getOriginalAmount()
        {
        return m_originalAmount;
        }

    public Money getConvertedAmount()
        {
        return m_convertedAmount;
        }

    public BigDecimal getExchangeRate()
        {
        return m_convertedAmount.getAmount()
                .divide(m_originalAmount.getAmount(), 4,
                        RoundingMode.HALF_EVEN);

        }


    // ---- data members ----------------------------------------------------

    private Money m_originalAmount;

    private Money m_convertedAmount;
    }
