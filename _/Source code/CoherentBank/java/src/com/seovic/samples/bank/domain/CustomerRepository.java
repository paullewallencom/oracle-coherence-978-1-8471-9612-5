package com.seovic.samples.bank.domain;


/**
 * @author Aleksandar Seovic  2009.11.30
 */
public interface CustomerRepository
    {
    void save(Customer customer);
    
    Customer getCustomer(long id);

    Customer findByUserName(String username);
    }
