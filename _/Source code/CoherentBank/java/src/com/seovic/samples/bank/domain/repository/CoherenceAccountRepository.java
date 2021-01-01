package com.seovic.samples.bank.domain.repository;


import com.seovic.coherence.util.AbstractCoherenceRepository;

import com.seovic.samples.bank.domain.AccountRepository;
import com.seovic.samples.bank.domain.Account;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;


/**
 * @author Aleksandar Seovic  2009.11.30
 */
@SuppressWarnings({"unchecked"})
public class CoherenceAccountRepository
        extends AbstractCoherenceRepository<Long, Account>
        implements AccountRepository
    {
    public Account getAccount(long id)
        {
        return get(id);
        }

    public NamedCache getCache()
        {
        return m_accounts;
        }

    private static final NamedCache m_accounts =
            CacheFactory.getCache("accounts");
    }