package com.seovic.samples.bank.domain;


import com.seovic.identity.SequenceGenerator;
import com.seovic.coherence.io.pof.AbstractPofSerializer;
import com.seovic.core.Entity;
import com.seovic.samples.bank.domain.services.SimpleCurrencyConverter;

import com.tangosol.io.AbstractEvolvable;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;

import java.io.IOException;
import java.util.Currency;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * @author Aleksandar Seovic  2009.11.29
 */
public class Account
        extends AbstractEvolvable
        implements Entity<Long>
    {
    // ---- constructors and factory methods --------------------------------

    public Account(long id, String description, Money balance,
                   long lastTransactionId, long customerId)
        {
        m_id                = id;
        m_description       = description;
        m_balance           = balance;
        m_lastTransactionId = lastTransactionId;
        m_customerId        = customerId;
        }

    static Account create(Customer customer, String description, Currency currency)
        {
        Account account = new Account(s_idGen.generateIdentity(), description,
                                      new Money(0, currency), 0, customer.getId());
        account.m_customer = customer;
        return account;
        }


    // ---- public methods --------------------------------------------------

    public Transaction withdraw(Money amount, String description)
            throws InsufficientFundsException
        {
        Money balance = m_balance;
        if (!balance.isSameCurrency(amount))
            {
            CurrencyConversion conversion = getCurrencyConverter().convert(amount, getCurrency());
            amount = conversion.getConvertedAmount();
            description += " (" + conversion.getOriginalAmount() + " @ " + conversion.getExchangeRate() + ")";
            }

        if (amount.greaterThan(balance))
            {
            throw new InsufficientFundsException(balance, amount);
            }

        m_balance = balance = balance.subtract(amount);
        return postTransaction(TransactionType.WITHDRAWAL, description, amount, balance);
        }

    public Transaction deposit(Money amount, String description)
        {
        Money balance = m_balance;
        if (!balance.isSameCurrency(amount))
            {
            CurrencyConversion conversion = getCurrencyConverter().convert(amount, getCurrency());
            amount = conversion.getConvertedAmount();
            description += " (" + conversion.getOriginalAmount() + " @ " + conversion.getExchangeRate() + ")";
            }

        m_balance = balance = balance.add(amount);
        return postTransaction(TransactionType.DEPOSIT, description, amount, balance);
        }

    public Collection<Transaction> getTransactions(Date from, Date to)
        {
        return getTransactionRepository().findTransactions(m_id, from, to);
        }

    @JsonIgnore
    public Collection<Transaction> getRecentTransactions()
        {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DAY_OF_MONTH, -30);

        // offset current time by one hour into the future, because current
        // time returned by system clock could go backwards
        return getTransactions(from.getTime(), new Date(new Date().getTime() + 3600000));
        }


    // ---- helper methods --------------------------------------------------

    protected Transaction postTransaction(TransactionType type, String description,
                                          Money amount, Money balance) 
        {
        Transaction transaction =
                Transaction.create(m_id, ++m_lastTransactionId, type, description, amount, balance);
        getTransactionRepository().save(transaction);
        return transaction;
        }


        // ---- dependencies ----------------------------------------------------

    /**
     * Customer repository.
     */
    private transient CustomerRepository m_customerRepository;

    @JsonIgnore
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

    /**
     * Transaction repository.
     */
    private transient TransactionRepository m_transactionRepository;

    @JsonIgnore
    protected TransactionRepository getTransactionRepository()
        {
        if (m_transactionRepository == null)
            {
            m_transactionRepository = RepositoryRegistry.getTransactionRepository();
            }
        return m_transactionRepository;
        }

    public void setTransactionRepository(TransactionRepository transactionRepository)
        {
        m_transactionRepository = transactionRepository;
        }

    /**
     * Currency converter.
     */
    private transient CurrencyConverter m_currencyConverter;

    @JsonIgnore
    public CurrencyConverter getCurrencyConverter()
        {
        if (m_currencyConverter == null)
            {
            m_currencyConverter = new SimpleCurrencyConverter();
            }
        return m_currencyConverter;
        }

    public void setCurrencyConverter(CurrencyConverter currencyConverter)
        {
        m_currencyConverter = currencyConverter;
        }


    // ---- properties ------------------------------------------------------

    public Long getId()
        {
        return m_id;
        }

    public String getDescription()
        {
        return m_description;
        }

    public void setDescription(String description)
        {
        m_description = description;
        }

    public Money getBalance()
        {
        return m_balance;
        }

    public Currency getCurrency()
        {
        return m_balance.getCurrency();
        }

    @JsonIgnore
    public long getLastTransactionId()
        {
        return m_lastTransactionId;
        }

    @JsonIgnore
    public long getCustomerId()
        {
        return m_customerId;
        }

    @JsonIgnore
    public Customer getCustomer()
        {
        Customer customer = m_customer;
        if (customer == null)
            {
            m_customer = customer = getCustomerRepository().getCustomer(m_customerId);
            }
        return customer;
        }


    // ---- AbstractEvolvable implementation --------------------------------

    @JsonIgnore
    public int getImplVersion()
        {
        return VERSION;
        }


    // ---- Object methods --------------------------------------------------

    public String toString()
        {
        return "Account{" +
               "id=" + m_id +
               ", description='" + m_description + '\'' +
               ", balance=" + m_balance +
               ", lastTransactionId=" + m_lastTransactionId +
               ", customerId=" + m_customerId +
               ", customer=" + m_customer +
               '}';
        }

    
    // ---- inner class: Serializer -----------------------------------------

    public static class Serializer
            extends AbstractPofSerializer<Account>
        {
        protected void serializeAttributes(Account obj, PofWriter writer)
                throws IOException
            {
            writer.writeLong  (0, obj.m_id);
            writer.writeString(1, obj.m_description);
            writer.writeObject(2, obj.m_balance);
            writer.writeLong  (3, obj.m_lastTransactionId);
            writer.writeLong  (4, obj.m_customerId);
            }

        protected Account createInstance(PofReader reader)
                throws IOException
            {
            return new Account(
                    reader.readLong(0),
                    reader.readString(1),
                    (Money) reader.readObject(2),
                    reader.readLong(3),
                    reader.readLong(4));
            }
        }


    // ---- static members --------------------------------------------------

    /**
     * Class version (for evolution support).
     */
    public static final int VERSION = 1;

    /**
     * Identity generator.
     */
    private static final SequenceGenerator s_idGen =
            SequenceGenerator.create("account.id");


    // ---- data members ----------------------------------------------------

    /**
     * Account ID.
     */
    private final long m_id;

    /**
     * Account description.
     */
    private String m_description;

    /**
     * Account balance.
     */
    private Money m_balance;

    /**
     * Last assigned transaction ID.
     */
    private long m_lastTransactionId;

    /**
     * Customer ID.
     */
    private final long m_customerId;

    /**
     * Customer this account belongs to.
     */
    private transient Customer m_customer;
    }
