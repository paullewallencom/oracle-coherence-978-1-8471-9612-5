/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.samples.bank.services;


import com.seovic.core.Command;
import com.seovic.coherence.util.EntryProcessorCommand;

import com.seovic.samples.bank.domain.Transaction;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Customer;

import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.processor.VersionedPut;
import com.tangosol.util.InvocableMap;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.net.CacheFactory;

import java.io.IOException;
import java.util.Currency;


/**
 * @author Aleksandar Seovic  2010.02.01
 */
public class CoherenceBankManager
        extends DefaultBankManager
    {
    protected Command<Customer> createUpdateProfileCommand(Customer customer)
        {
        return new EntryProcessorCommand<Customer>(
                s_customersCache, customer.getId(),
                new VersionedPut(customer));
        }

    protected Command<Account> createOpenAccountCommand(Customer customer,
                                                        String description,
                                                        Currency currency)
        {
        return new EntryProcessorCommand<Account>(
                s_customersCache, customer.getId(),
                new OpenAccountProcessor(description, currency));
        }

    protected Command<Transaction> createWithdrawalCommand(long accountId,
                                                           Money amount,
                                                           String description)
        {
        return new EntryProcessorCommand<Transaction>(
                s_accountsCache, accountId,
                new WithdrawalProcessor(amount, description));
        }

    protected Command<Transaction> createDepositCommand(long accountId,
                                                        Money amount,
                                                        String description)
        {
        return new EntryProcessorCommand<Transaction>(
                s_accountsCache, accountId,
                new DepositProcessor(amount, description));
        }


    // ---- inner class: OpenAccountProcessor -------------------------------

    public static class OpenAccountProcessor
            extends AbstractProcessor
            implements PortableObject
        {
        public OpenAccountProcessor()
            {
            }

        public OpenAccountProcessor(String description, Currency currency)
            {
            m_description = description;
            m_currency    = currency;
            }

        public Object process(InvocableMap.Entry entry)
            {
            Customer customer = (Customer) entry.getValue();
            if (customer == null)
                {
                throw new IllegalArgumentException("The specified customer does not exist.");
                }

            Account account = customer.openAccount(m_description, m_currency);
            entry.setValue(customer, false);

            return account;
            }

        public void readExternal(PofReader reader)
                throws IOException
            {
            m_description = reader.readString(0);
            m_currency    = (Currency) reader.readObject(1);
            }

        public void writeExternal(PofWriter writer)
                throws IOException
            {
            writer.writeString(0, m_description);
            writer.writeObject(1, m_currency);
            }

        private String   m_description;
        private Currency m_currency;
        }


    // ---- inner class: AccountProcessor -----------------------------------

    private static abstract class AccountProcessor
            extends AbstractProcessor
            implements PortableObject
        {
        protected AccountProcessor()
            {
            }

        protected AccountProcessor(Money amount, String description)
            {
            m_amount = amount;
            m_description = description;
            }

        protected abstract Object processAccount(Account account,
                                                 Money amount,
                                                 String description);

        public Object process(InvocableMap.Entry entry)
            {
            Account account = (Account) entry.getValue();
            if (account == null)
                {
                throw new IllegalArgumentException("The specified account does not exist.");
                }

            Object result = processAccount(account, m_amount, m_description);
            entry.setValue(account, false);

            return result;
            }

        public void readExternal(PofReader reader)
                throws IOException
            {
            m_amount      = (Money) reader.readObject(0);
            m_description = reader.readString(1);
            }

        public void writeExternal(PofWriter writer)
                throws IOException
            {
            writer.writeObject(0, m_amount);
            writer.writeString(1, m_description);
            }

        protected Money  m_amount;
        protected String m_description;
        }


    // ---- inner class: WithdrawalProcessor --------------------------------

    public static class WithdrawalProcessor
            extends AccountProcessor
        {
        public WithdrawalProcessor()
            {
            }

        public WithdrawalProcessor(Money amount, String description)
            {
            super(amount, description);
            }

        protected Object processAccount(Account account, Money amount,
                                         String description)
            {
            return account.withdraw(amount, description);
            }
        }


    // ---- inner class: DepositProcessor -----------------------------------

    public static class DepositProcessor
            extends AccountProcessor
        {
        public DepositProcessor()
            {
            }

        public DepositProcessor(Money amount, String description)
            {
            super(amount, description);
            }

        protected Object processAccount(Account account, Money amount,
                                      String description)
            {
            return account.deposit(amount, description);
            }
        }


    // ---- static members --------------------------------------------------

    private static final InvocableMap s_customersCache = CacheFactory.getCache("customers");
    private static final InvocableMap s_accountsCache  = CacheFactory.getCache("accounts");
    }
