package com.seovic.samples.bank.domain;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;


/**
 * An exception that will be raised if the balance in the account is
 * not sufficient to cover the withdrawal.
 *
 * @author Aleksandar Seovic  2009.02.07
 */
public class InsufficientFundsException
        extends RuntimeException
        implements PortableObject
    {
    // ---- constructors ----------------------------------------------------

    public InsufficientFundsException(Money balance, Money withdrawalAmount)
        {
        super("Attempted to withdraw " + withdrawalAmount
              + ". Available balance is " + balance);
        m_balance          = balance;
        m_withdrawalAmount = withdrawalAmount;
        }


    // ---- properties ------------------------------------------------------

    public Money getBalance()
        {
        return m_balance;
        }

    public Money getWithdrawalAmount()
        {
        return m_withdrawalAmount;
        }


    // ---- PortableObject implementation -----------------------------------

    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_balance          = (Money) pofReader.readObject(0);
        m_withdrawalAmount = (Money) pofReader.readObject(1);
        }

    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeObject(0, m_balance);
        pofWriter.writeObject(1, m_withdrawalAmount);
        }


    // ---- data members ----------------------------------------------------

    private Money m_balance;
    private Money m_withdrawalAmount;
    }