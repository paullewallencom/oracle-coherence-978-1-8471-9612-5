package com.seovic.samples.bank.domain;


import static org.junit.Assert.*;
import org.junit.Test;

import com.seovic.samples.bank.domain.repository.FakeAccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Aleksandar Seovic  2009.11.29
 */
public class CustomerTests
    {
    @Test
    public void testCustomerCreation()
        {
        Customer c = Customer.create("Aleksandar Seovic", "aleks", "pass", "aleks@nospam.com");

        assertEquals("Aleksandar Seovic", c.getName());
        assertEquals("aleks@nospam.com", c.getEmail());
        assertTrue(c.getId() > 0);
        assertNotNull(c.getAddress());
        }

    @Test
    public void testAccountSetup()
        {
        Customer c = Customer.create("Aleksandar Seovic", "aleks", "pass", "aleks@nospam.com");
        c.setAccountRepository(new FakeAccountRepository());

        Account a = c.openAccount("Checking", Currencies.USD);

        assertNotNull(a);
        assertEquals(c, a.getCustomer());
        assertEquals("Checking", a.getDescription());
        assertEquals(Currencies.USD, a.getCurrency());
        }

    @Test
    public void testAccountRetrieval()
        {
        Customer c = Customer.create("Aleksandar Seovic", "aleks", "pass", "aleks@nospam.com");
        c.setAccountRepository(new FakeAccountRepository());

        c.openAccount("Checking", Currencies.USD);
        c.openAccount("Checking", Currencies.EUR);
        c.openAccount("Savings", Currencies.USD);

        assertEquals(3, c.getAccounts().size());
        }

    @Test
    public void testBigDecimal()
        {
        BigDecimal num = BigDecimal.valueOf(21527, 2);
        assertEquals(new BigDecimal("215.27"), num);
        assertEquals(21527L, num.unscaledValue().longValue());
        assertEquals(2, num.scale());

        BigDecimal num2 = num.multiply(new BigDecimal("1.5008"));
        num2 = num2.setScale(2, RoundingMode.HALF_EVEN);
        System.out.println(num2);
        }
    }
