package com.seovic.samples.bank.domain;


import java.util.Date;
import java.util.Collection;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public interface TransactionRepository
    {
    void save(Transaction transaction);

    Collection<Transaction> findTransactions(long accountId, Date from, Date to);
    }
