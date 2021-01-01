package com.seovic.samples.bank.domain;


import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import com.seovic.samples.bank.domain.repository.FakeAccountRepository;
import com.seovic.samples.bank.domain.repository.FakeExchangeRateRepository;
import com.seovic.samples.bank.domain.repository.FakeCustomerRepository;
import com.seovic.samples.bank.domain.repository.FakeTransactionRepository;
import static com.seovic.samples.bank.domain.Currencies.*;
import com.seovic.samples.bank.domain.services.SimpleCurrencyConverter;
import java.util.Collection;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
public class AccountTests
    {
    @Before
    public void setup()
        {
        Customer c = Customer.create("Aleksandar Seovic", "aleks", "pass", "aleks@nospam.com");
        c.setAccountRepository(new FakeAccountRepository());

        account = c.openAccount("Checking", USD);
        account.setCurrencyConverter(new SimpleCurrencyConverter(new FakeExchangeRateRepository()));
        account.setCustomerRepository(new FakeCustomerRepository());
        account.setTransactionRepository(new FakeTransactionRepository());
        }

    @After
    public void printTransactions()
        {
        printTransactions(account.getRecentTransactions());
        }

    @Test
    public void testDeposit()
        {
        account.deposit(new Money("100.25", USD), "Branch deposit");
        assertEquals(new Money("100.25", USD), account.getBalance());
        }

    @Test
    public void testForeignCurrencyDeposit()
        {
        account.deposit(new Money(100, EUR), "Branch deposit");
        assertEquals(new Money("150.83", USD), account.getBalance());
        }

    @Test
    public void testWithdrawal()
        {
        account.deposit(new Money("100.25", USD), "Branch deposit");
        account.withdraw(new Money("15.05", USD), "ATM withdrawal");
        assertEquals(new Money("85.20", USD), account.getBalance());
        }

    @Test
    public void testForeignCurrencyWithdrawal()
        {
        account.deposit(new Money("150.83", USD), "Branch deposit");
        account.withdraw(new Money(100, EUR), "ATM withdrawal");
        assertEquals(new Money(0, USD), account.getBalance());
        }

    @Test(expected = InsufficientFundsException.class)
    public void testInsufficientFundsWithdrawal()
        {
        account.withdraw(new Money("15.05", USD), "ATM withdrawal");
        }

    private void printTransactions(Collection<Transaction> transactions)
        {
        for (Transaction tx : transactions)
            {
            System.out.println(tx);
            }
        }
    
    private Account account;
    }
