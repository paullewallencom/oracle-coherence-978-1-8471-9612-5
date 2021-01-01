package com.seovic.samples.bank.domain.persistence;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.springframework.jdbc.core.JdbcTemplate;

import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.persistence.AccountJdbcCacheStore;

import java.util.Map;
import java.util.Collections;


/**
 * Tests for AccountJdbcDao
 *
 * @author pperalta Jan 30, 2010
 */
public class AccountJdbcCacheStoreTests
        extends AbstractJdbcCacheStoreTests
    {
    @Test
    public void accountDaoTest()
        {
        AccountJdbcCacheStore accountDao = new AccountJdbcCacheStore(getDataSource());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        Account account = new Account(1L, "description", new Money(1000, "USD"), 0L, 1L);

        accountDao.store(account.getId(), account);

        Map results = jdbcTemplate.queryForMap("select * from accounts");

        assertEquals(account.getId(), results.get("ID"));
        assertEquals(account.getDescription(), results.get("DESCRIPTION"));
        assertEquals(account.getBalance().getAmount(), results.get("BALANCE"));

        Account fromDbAccount = (Account) accountDao.load(1L);

        assertEquals(account.toString(), fromDbAccount.toString());

        account.setDescription("modified");

        accountDao.storeAll(Collections.singletonMap(account.getId(), account));

        fromDbAccount = (Account) accountDao.load(1L);

        assertEquals(account.toString(), fromDbAccount.toString());
        }
    }
