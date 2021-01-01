package com.seovic.samples.bank.domain;


import java.util.Collection;


/**
 * @author Aleksandar Seovic  2009.11.29
 */
public interface AccountRepository
    {
    Account getAccount(long id);

    Collection<Account> getAll(Collection<Long> accountIds);

    void save(Account account);
    }
