package com.seovic.samples.bank.domain;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;
import java.util.Currency;


/**
 * An exception that will be raised if an unsupported currency is specified.
 *
 * @author Aleksandar Seovic  2010.02.08
 */
public class UnsupportedCurrencyException
        extends RuntimeException
        implements PortableObject
    {
    private Currency m_currency;

    public UnsupportedCurrencyException(Currency currency)
        {
        super("Currency " + currency.getCurrencyCode() + " is not supported." +
              "\nCurrently supported currencies are USD, EUR, GBP, CHF, CAD, AUD and JPY.");
        m_currency = currency;
        }

    public Currency getCurrency()
        {
        return m_currency;
        }

    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_currency = (Currency) pofReader.readObject(0);
        }

    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeObject(0, m_currency);
        }
    }