package com.seovic.samples.bank.domain.persistence;


import com.seovic.samples.bank.domain.Address;
import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.persistence.CustomerJdbcCacheStore;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;


/**
 * Tests for CustomerJdbcDao.
 *
 * @author pperalta Jan 30, 2010
 */
public class CustomerJdbcCacheStoreTests
        extends AbstractJdbcCacheStoreTests
    {
    @Test
    public void customerDaoTest()
        {
        CustomerJdbcCacheStore customerDao = new CustomerJdbcCacheStore(getDataSource());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        Customer customer = new Customer(1L, "name", "username", "password",
                "email", new Address("street", "city", "state", "zip", "us"),
                new ArrayList<Long>(0), 1);

        customerDao.store(customer.getId(), customer);

        Map results = jdbcTemplate.queryForMap("select * from customers where id = 1");

        assertEquals(customer.getId(), results.get("ID"));
        assertEquals(customer.getName(), results.get("NAME"));
        assertEquals(customer.getAddress().getCity(), results.get("CITY"));

        Customer fromDbCustomer = (Customer) customerDao.load(1L);

        assertEquals(customer.toString(), fromDbCustomer.toString());

        customer.setName("modified");

        customerDao.storeAll(Collections.singletonMap(customer.getId(), customer));

        fromDbCustomer = (Customer) customerDao.load(1L);

        assertEquals(customer.toString(), fromDbCustomer.toString());
        }
    }
