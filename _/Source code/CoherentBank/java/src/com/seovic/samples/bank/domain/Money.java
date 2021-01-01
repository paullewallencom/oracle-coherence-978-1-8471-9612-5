package com.seovic.samples.bank.domain;


import com.seovic.coherence.io.pof.AbstractPofSerializer;

import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;

import java.io.Serializable;
import java.io.IOException;

import java.util.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class Money implements Serializable
    {
    // ---- constructors ----------------------------------------------------

    public Money(String amount, String currencyCode)
        {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
        }

    public Money(String amount, Currency currency)
        {
        this(new BigDecimal(amount), currency);
        }

    public Money(int amount, String currencyCode)
        {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
        }

    public Money(int amount, Currency currency)
        {
        this(new BigDecimal(amount), currency);
        }

    public Money(long amount, String currencyCode)
        {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
        }

    public Money(long amount, Currency currency)
        {
        this(new BigDecimal(amount), currency);
        }

    public Money(double amount, String currencyCode)
        {
        this(new BigDecimal(amount), Currency.getInstance(currencyCode));
        }

    public Money(double amount, Currency currency)
        {
        this(new BigDecimal(amount), currency);
        }

    public Money(BigDecimal amount, String currencyCode)
        {
        this(amount, Currency.getInstance(currencyCode));
        }

    public Money(BigDecimal amount, Currency currency)
        {
        m_amount   = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
        m_currency = currency;
        }


    // ---- properties ------------------------------------------------------

    public BigDecimal getAmount()
        {
        return m_amount;
        }

    public Currency getCurrency()
        {
        return m_currency;
        }


    // ---- public methods --------------------------------------------------

    public boolean isSameCurrency(Money money)
        {
        return m_currency.equals(money.m_currency);
        }

    public Money add(Money money)
        {
        checkCurrency(money);
        return new Money(m_amount.add(money.m_amount), m_currency);
        }

    public Money subtract(Money money)
        {
        checkCurrency(money);
        return new Money(m_amount.subtract(money.m_amount), m_currency);
        }

    public boolean greaterThan(Money money)
        {
        checkCurrency(money);
        return m_amount.compareTo(money.m_amount) > 0;
        }

    public boolean lessThan(Money money)
        {
        checkCurrency(money);
        return m_amount.compareTo(money.m_amount) < 0;
        }


    // ---- helper methods --------------------------------------------------

    protected void checkCurrency(Money money)
        {
        if (!isSameCurrency(money))
            {
            throw new IllegalArgumentException("Currencies are not the same");
            }
        }


    // ---- Object methods --------------------------------------------------

    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        Money money = (Money) o;

        return m_amount.equals(money.m_amount)
               && m_currency.equals(money.m_currency);
        }

    public int hashCode()
        {
        int result = m_amount.hashCode();
        result = 31 * result + m_currency.hashCode();
        return result;
        }

    public String toString()
        {
        return m_currency.getSymbol() + " " + m_amount;
        }


    // ---- inner class: Serializer -----------------------------------------

    public static class Serializer extends AbstractPofSerializer<Money>
        {
        protected void serializeAttributes(Money money, PofWriter writer)
                throws IOException
            {
            // C++ client does not support BigDecimal at the moment,
            // so we need to implement custom serialization logic
            writer.writeLong  (0, money.m_amount.unscaledValue().longValue());
            writer.writeInt   (1, money.m_amount.scale());
            writer.writeObject(2, money.m_currency);
            }

        protected Money createInstance(PofReader reader)
                throws IOException
            {
            long     unscaledValue = reader.readLong(0);
            int      scale         = reader.readInt(1);
            Currency currency      = (Currency) reader.readObject(2);

            return new Money(BigDecimal.valueOf(unscaledValue, scale), currency);
            }
        }


    // ---- data members ----------------------------------------------------

    private final BigDecimal m_amount;
    private final Currency m_currency;
    }
