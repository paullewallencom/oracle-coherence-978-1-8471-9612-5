package com.seovic.samples.bank.domain;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public enum TransactionType
    {
    DEPOSIT,
    WITHDRAWAL;

    /**
     * JavaBean compliant property accessor for String value of enum.
     * This is required by Spring JDBC.
     *
     * @return name of enum as a String
     */
    public String getName()
        {
        return name();
        }
    }
