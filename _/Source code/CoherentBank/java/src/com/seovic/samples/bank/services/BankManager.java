package com.seovic.samples.bank.services;


import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.Account;
import java.util.Currency;


/**
 * @author Aleksandar Seovic  2009.11.30
 */
public interface BankManager
    {
    Customer authenticate(String username, String password);

    void updateProfile(Customer customer);

    Account openAccount(Customer customer, String description, Currency currency);
    
    void postWithdrawal(Customer customer, long accountId,
                        String description, Money amount);

    void postDeposit(Customer customer, long accountId,
                     String description, Money amount);

    void postBillPayment(Customer customer, long accountId, String payee,
                         String description, Money amount);
    }
