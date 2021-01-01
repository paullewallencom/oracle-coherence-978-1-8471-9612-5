package com.seovic.samples.bank;


import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.Currencies;
import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.RepositoryRegistry;
import com.seovic.samples.bank.services.BankManager;

import java.util.Currency;
import java.util.Random;
import java.math.BigDecimal;

import org.springframework.beans.factory.InitializingBean;


/**
 * @author Aleksandar Seovic  2009.12.04
 */
public class TestDataCreator implements InitializingBean
    {
    private static final Random rnd = new Random();

    private BankManager bankManager;
    public void setBankManager(BankManager bankManager)
        {
        this.bankManager = bankManager;
        }

    public void afterPropertiesSet()
            throws Exception
        {
        createTestData();
        }

    public void createTestData()
        {
        // create some test customers
        createTestCustomer("Aleksandar Seovic", "sele", "pass", "sele@nospam.com");
        createTestCustomer("Aleksandar Jevic",  "aca", "pass", "aca@nospam.com");
        createTestCustomer("Marko Dumic", "marko", "pass", "marko@nospam.com");
        createTestCustomer("Ivan Cikic", "ivan", "pass", "ivan@nospam.com");
        createTestCustomer("Mark Falco", "mark", "pass", "mark@nospam.com");
        createTestCustomer("Patrick Peralta", "patrick", "pass", "patrick@nospam.com");
        }

    private void createTestCustomer(String name, String username, String password, String email)
        {
        Customer customer = Customer.create(name, username, password, email);
        RepositoryRegistry.getCustomerRepository().save(customer);

        createTestAccount(customer, "Checking", Currencies.USD, new BigDecimal(rnd.nextInt(100000)));
        createTestAccount(customer, "Savings", Currencies.EUR, new BigDecimal(rnd.nextInt(100000)));
        }

    private void createTestAccount(Customer customer, String description, Currency currency, BigDecimal deposit)
        {
        Account account = bankManager.openAccount(customer, description, currency);
        bankManager.postDeposit(customer, account.getId(), "Initial deposit", new Money(deposit, currency));
        }
    }
