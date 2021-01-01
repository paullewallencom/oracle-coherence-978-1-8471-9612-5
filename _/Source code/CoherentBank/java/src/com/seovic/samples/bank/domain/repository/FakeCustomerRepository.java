package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.CustomerRepository;
import com.seovic.samples.bank.domain.Customer;

import java.util.Map;
import java.util.HashMap;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
public class FakeCustomerRepository implements CustomerRepository
    {
    public FakeCustomerRepository()
        {
        m_customers = new HashMap<Long, Customer>();

        // create some test customers
        save(Customer.create("Aleksandar Seovic", "sele", "pass", "sele@nospam.com"));
        save(Customer.create("Aleksandar Jevic",  "aca", "pass", "aca@nospam.com"));
        save(Customer.create("Marko Dumic", "marko", "pass", "marko@nospam.com"));
        save(Customer.create("Ivan Cikic", "ivan", "pass", "ivan@nospam.com"));
        }

    public void save(Customer customer)
        {
        m_customers.put(customer.getId(), customer);
        }

    public Customer getCustomer(long id)
        {
        return m_customers.get(id);
        }

    public Customer findByUserName(String username)
        {
        for (Customer customer : m_customers.values())
            {
            if (username.equals(customer.getUsername()))
                {
                return customer;
                }
            }
        return null;
        }

    private final Map<Long, Customer> m_customers;
    }
