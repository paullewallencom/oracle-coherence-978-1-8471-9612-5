package com.seovic.samples.bank.domain.persistence;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.springframework.jdbc.core.JdbcTemplate;

import com.seovic.samples.bank.domain.Transaction;
import com.seovic.samples.bank.domain.TransactionType;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.persistence.TransactionJdbcCacheStore;

import java.util.Map;


/**
 * Tests for TransactionJdbcDao
 *
 * @author pperalta Jan 30, 2010
 */
public class TransactionJdbcCacheStoreTests
        extends AbstractJdbcCacheStoreTests
    {
    @Test
    public void transactionDaoTest()
        {
        TransactionJdbcCacheStore transactionDao =
                new TransactionJdbcCacheStore(getDataSource());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        Transaction tx = new Transaction(new Transaction.Id(1L, 2L),
                TransactionType.DEPOSIT, new java.util.Date(), "description",
                new Money(100, "USD"), new Money(100, "USD"));

        transactionDao.store(tx.getId(), tx);

        Map results = jdbcTemplate.queryForMap("select * from transactions " +
                "where account_id = 1 and id = 2");

        assertEquals(tx.getDescription(), results.get("DESCRIPTION"));

        Transaction fromDbTx = (Transaction)
                transactionDao.load(new Transaction.Id(1L, 2L));

        assertEquals(tx.toString(), fromDbTx.toString());
        }
    }
