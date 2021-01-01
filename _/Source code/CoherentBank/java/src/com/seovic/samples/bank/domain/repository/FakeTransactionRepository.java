package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.TransactionRepository;
import com.seovic.samples.bank.domain.Transaction;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class FakeTransactionRepository
        implements TransactionRepository
    {
    public void save(Transaction transaction)
        {
        m_transactions.put(transaction.getId(), transaction);
        }

    public Collection<Transaction> findTransactions(long accountId, Date from, Date to)
        {
        List<Transaction> result = new ArrayList<Transaction>();
        for (Transaction tx : m_transactions.values())
            {
            if (tx.getId().getAccountId() == accountId
                    && tx.getTime().getTime() >= from.getTime()
                    && tx.getTime().getTime() <= to.getTime())
                {
                result.add(tx);
                }
            }

        Collections.sort(result, new Comparator<Transaction>()
            {
            public int compare(Transaction t1, Transaction t2)
                {
                return t1.getTime().compareTo(t2.getTime());
                }
            });

        return result;
        }

    private Map<Transaction.Id, Transaction> m_transactions
            = new HashMap<Transaction.Id, Transaction>();
    }