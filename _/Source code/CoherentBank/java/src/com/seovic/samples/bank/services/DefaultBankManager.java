package com.seovic.samples.bank.services;


import com.seovic.core.Command;

import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.CustomerRepository;
import com.seovic.samples.bank.domain.RepositoryRegistry;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.AccountRepository;
import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Transaction;
import java.util.Currency;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
public class DefaultBankManager
        implements BankManager
    {
    // ---- BankManager implementation --------------------------------------

    public Customer authenticate(String username, String password)
        {
        Customer customer = getCustomerRepository().findByUserName(username);
        return customer != null
               && password.equals(customer.getPassword()) ? customer : null;
        }

    public void updateProfile(Customer customer)
        {
        Command<Customer> updateProfile = createUpdateProfileCommand(customer);
        updateProfile.execute();
        }

    public Account openAccount(Customer customer, String description,
                               Currency currency)
        {
        Command<Account> openAccount = createOpenAccountCommand(
                customer, description, currency);
        return openAccount.execute();
        }

    public void postWithdrawal(Customer customer, long accountId,
                               String description, Money amount)
        {
        if (!customer.isOwnedAccount(accountId))
            {
            throw new SecurityException("Use your own money!");
            }

        Command withdraw = createWithdrawalCommand(accountId, amount, description);
        withdraw.execute();
        }

    public void postDeposit(Customer customer, long accountId,
                            String description, Money amount)
        {
        Command deposit = createDepositCommand(accountId, amount, description);
        deposit.execute();
        }

    public void postBillPayment(Customer customer, long accountId, String payee,
                                String description, Money amount)
        {
        postWithdrawal(customer, accountId,
                       "Bill payment: " + payee + " / " + description, amount);
        }


    // ---- dependencies ----------------------------------------------------

    private CustomerRepository m_customerRepository;

    protected CustomerRepository getCustomerRepository()
        {
        if (m_customerRepository == null)
            {
            m_customerRepository = RepositoryRegistry.getCustomerRepository();
            }
        return m_customerRepository;
        }

    public void setCustomerRepository(CustomerRepository customerRepository)
        {
        m_customerRepository = customerRepository;
        }

    private AccountRepository m_accountRepository;

    protected AccountRepository getAccountRepository()
        {
        if (m_accountRepository == null)
            {
            m_accountRepository = RepositoryRegistry.getAccountRepository();
            }
        return m_accountRepository;
        }

    public void setAccountRepository(AccountRepository accountRepository)
        {
        m_accountRepository = accountRepository;
        }


    // ---- helper methods --------------------------------------------------

    protected Command<Customer> createUpdateProfileCommand(Customer customer)
        {
        return new UpdateProfileCommand(customer);
        }

    protected Command<Account> createOpenAccountCommand(Customer customer,
            String description, Currency currency)
        {
        return new OpenAccountCommand(customer, description, currency);
        }

    protected Command<Transaction> createWithdrawalCommand(
            long accountId, Money amount, String description)
        {
        Account account = getAccountRepository().getAccount(accountId);
        return new WithdrawalCommand(account, amount, description);
        }

    protected Command<Transaction> createDepositCommand(
            long accountId, Money amount, String description)
        {
        Account account = getAccountRepository().getAccount(accountId);
        return new DepositCommand(account, amount, description);
        }


    // ---- inner class: UpdateProfileCommand -------------------------------

    private class UpdateProfileCommand
            implements Command<Customer>
        {
        private UpdateProfileCommand(Customer customer)
            {
            m_customer = customer;
            }

        public Customer execute()
            {
            getCustomerRepository().save(m_customer);
            return m_customer;
            }

        private Customer m_customer;
        }


    // ---- inner class: OpenAccountCommand -------------------------------

    private class OpenAccountCommand
            implements Command<Account>
        {
        private OpenAccountCommand(Customer customer, String description, Currency currency)
            {
            m_customer    = customer;
            m_description = description;
            m_currency    = currency;
            }

        public Account execute()
            {
            return m_customer.openAccount(m_description, m_currency);
            }

        private Customer m_customer;
        private String   m_description;
        private Currency m_currency;
        }


    // ---- inner class: WithdrawalCommand ----------------------------------

    private static class WithdrawalCommand
            implements Command<Transaction>
        {
        private WithdrawalCommand(Account account, Money amount, String description)
            {
            m_account = account;
            m_amount = amount;
            m_description = description;
            }

        public Transaction execute()
            {
            return m_account.withdraw(m_amount, m_description);
            }

        private Account m_account;
        private Money m_amount;
        private String m_description;
        }

    
    // ---- inner class: WithdrawalCommand ----------------------------------

    private static class DepositCommand
            implements Command<Transaction>
        {
        private DepositCommand(Account account, Money amount, String description)
            {
            m_account = account;
            m_amount = amount;
            m_description = description;
            }

        public Transaction execute()
            {
            return m_account.deposit(m_amount, m_description);
            }

        private Account m_account;
        private Money m_amount;
        private String m_description;
        }
    }
