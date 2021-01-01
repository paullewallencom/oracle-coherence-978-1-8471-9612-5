package com.seovic.samples.bank.domain.repository;


import com.seovic.coherence.util.AbstractCoherenceRepository;

import com.seovic.samples.bank.domain.CustomerRepository;
import com.seovic.samples.bank.domain.Customer;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.filter.EqualsFilter;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
@SuppressWarnings({"unchecked"})
public class CoherenceCustomerRepository
        extends AbstractCoherenceRepository<Long, Customer>
        implements CustomerRepository
    {
    public Customer getCustomer(long id)
        {
        return get(id);
        }

    public Customer findByUserName(String username)
        {
        return queryForSingleValue(new EqualsFilter("getUsername", username));
        }

    public NamedCache getCache()
        {
        return m_customers;
        }

    private static final NamedCache m_customers =
            CacheFactory.getCache("customers");
    }