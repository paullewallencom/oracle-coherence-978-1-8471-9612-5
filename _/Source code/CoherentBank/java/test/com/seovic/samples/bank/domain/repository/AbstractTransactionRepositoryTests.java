package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.TransactionRepository;
import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.Transaction;
import com.seovic.samples.bank.domain.services.SimpleCurrencyConverter;
import static com.seovic.samples.bank.domain.Currencies.USD;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.Collection;
import java.util.Date;


/**
 * @author Aleksandar Seovic  2009.12.04
 */
public abstract class AbstractTransactionRepositoryTests
    {
    protected abstract TransactionRepository createTransactionRepository();

    @Before
    public void setup()
        {
        txRep = createTransactionRepository();
        
        Customer c = Customer.create("Aleksandar Seovic", "aleks", "pass", "aleks@nospam.com");
        c.setAccountRepository(new FakeAccountRepository());

        account = c.openAccount("Checking", USD);
        account.setCurrencyConverter(new SimpleCurrencyConverter(new FakeExchangeRateRepository()));
        account.setCustomerRepository(new FakeCustomerRepository());
        account.setTransactionRepository(txRep);

        account.deposit(new Money(100, USD), "initial deposit");
        account.deposit(new Money(200, USD), "ATM deposit");
        account.withdraw(new Money(150, USD), "cash withdrawal");
        }

    @SuppressWarnings({"deprecation"})
    @Test
    public void testTransactionRetrieval()
        {
        Collection<Transaction> transactions =
                txRep.findTransactions(account.getId(), new Date(109, 0, 1), new Date(new Date().getTime() + 3600000));

        printTransactions(transactions);
        assertEquals(3, transactions.size());
        }

    protected void printTransactions(Collection<Transaction> transactions)
        {
        for (Transaction tx : transactions)
            {
            System.out.println(tx);
            }
        }

    private TransactionRepository txRep;
    private Account account;
    }
