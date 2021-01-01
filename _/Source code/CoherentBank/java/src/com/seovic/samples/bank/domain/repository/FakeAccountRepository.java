package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.AccountRepository;
import com.seovic.samples.bank.domain.Account;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


/**
 * @author Aleksandar Seovic  2009.11.30
 */
public class FakeAccountRepository implements AccountRepository
    {
    public Account getAccount(long id)
        {
        return m_accounts.get(id);
        }

    public Collection<Account> getAll(Collection<Long> accountIds)
        {
        Collection<Account> result = new ArrayList<Account>(accountIds.size());
        for (Long id : accountIds)
            {
            Account account = m_accounts.get(id);
            if (account != null)
                {
                result.add(account);
                }
            }
        return result;
        }

    public void save(Account account)
        {
        m_accounts.put(account.getId(), account);
        }

    private Map<Long, Account> m_accounts = new HashMap<Long, Account>();
    }
