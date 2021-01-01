package com.seovic.samples.bank.domain.repository;


import com.seovic.coherence.util.filter.FilterBuilder;
import com.seovic.coherence.util.AbstractCoherenceRepository;

import com.seovic.samples.bank.domain.TransactionRepository;
import com.seovic.samples.bank.domain.Transaction;

import com.tangosol.util.filter.KeyAssociatedFilter;
import com.tangosol.util.Filter;
import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.util.Collection;
import java.util.Date;
import java.util.Comparator;
import java.util.Calendar;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
@SuppressWarnings({"unchecked"})
public class CoherenceTransactionRepository
        extends AbstractCoherenceRepository<Transaction.Id, Transaction>
        implements TransactionRepository
    {
    // ---- TransactionRepository implementation ----------------------------

    @Override
    public void save(Transaction tx) {
        getBackingMap().put(tx.getId(), tx);
    }

    public Collection<Transaction> findTransactions(long accountId, Date from, Date to)
        {
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(to);
        calTo.add(Calendar.DAY_OF_MONTH, 1);

        Filter filter = new FilterBuilder()
                .equals("id.accountId", accountId)
                .between("time", from, calTo.getTime())
                .toAnd();

        return queryForValues(new KeyAssociatedFilter(filter, accountId),
                              new DefaultTransactionComparator());
        }

    public NamedCache getCache()
        {
        return m_transactions;
        }


    // ---- inner class: DefaultTransactionComparator -----------------------

    /**
     * Default transaction comparator that sorts transactions by
     * transaction number.
     */
    private static class DefaultTransactionComparator
            implements Comparator<Transaction>
        {
        public int compare(Transaction t1, Transaction t2)
            {
            long txNum1 = t1.getId().getTransactionNumber();
            long txNum2 = t2.getId().getTransactionNumber();

            return txNum1 == txNum2
                   ? 0
                   : txNum1 > txNum2 ? 1 : -1;
            }
        }


    // ---- static members --------------------------------------------------

    private static final NamedCache m_transactions =
            CacheFactory.getCache("transactions");
    }