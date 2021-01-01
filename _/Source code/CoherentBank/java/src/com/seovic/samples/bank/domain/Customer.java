package com.seovic.samples.bank.domain;


import com.tangosol.io.AbstractEvolvable;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;
import com.tangosol.util.Versionable;

import com.seovic.identity.SequenceGenerator;
import com.seovic.coherence.io.pof.AbstractPofSerializer;
import com.seovic.core.Entity;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Currency;
import javax.servlet.http.HttpSession;


/**
 * Customer object.
 * 
 * @author Aleksandar Seovic  2009.11.29
 */
public class Customer
        extends AbstractEvolvable
        implements Entity<Long>, Versionable
    {
    // ---- constructors and factory methods --------------------------------

    /**
     * This constructor is for convinience only, as it allows Spring MVC to
     * bind request data to a Customer instance.
     * <p/>
     * Instances created using this constructor are invalid and should never be
     * stored in the cache directly. Instead, you should either create a proper
     * Customer instance using {@link #create(String, String, String, String)}
     * factory method, or obtain an existing instance from the cache and update
     * it using the data from the transient instance created using this
     * constructor.
     * <p/>
     * An example of such use can be seen in
     * {@link com.seovic.samples.bank.web.controllers.DashboardController#updateProfile(Customer, HttpSession)}
     * method.
     */
    public Customer()
        {
        m_address = new Address();
        }

    /**
     * This constructor is used internally from the factory method below to
     * initialize new Customer objects, as well as from the deserializer and
     * cache loader to create and rehydrate existing Customer objects.
     * <p/>
     * It should never be called directly by the application code.
     *
     * @param id          customer id
     * @param name        customer's name
     * @param username    login username
     * @param password    login password
     * @param email       customer's email
     * @param address     customer's mailing address
     * @param accountIds  customer's account identifiers
     * @param version     object version, used for optimistic locking
     */
    public Customer(long id, String name, String username, String password,
                     String email, Address address, Collection<Long> accountIds,
                     int version)
        {
        m_id         = id;
        m_name       = name;
        m_username   = username;
        m_password   = password;
        m_email      = email;
        m_address    = address;
        m_accountIds = accountIds;
        m_version    = version;
        }

    /**
     * Static factory method that should always be used to create new
     * Customers.
     *
     * @param name      customer's name
     * @param username  login username
     * @param password  login password
     * @param email     customer's email
     *
     * @return new Customer instance
     */
    public static Customer create(String name, String username,
                                  String password, String email)
        {
        return new Customer(s_idGen.generateIdentity(), name, username, password,
                            email, new Address(), new ArrayList<Long>(), 1);
        }


    // ---- public methods --------------------------------------------------

    /**
     * Opens a new account for the customer.
     * <p/>
     * Coherence note: Because this is a mutating operation, it has to be
     * executed on the storage node for this Customer instance using an
     * entry processor.
     *
     * @param description  account description
     * @param currency     account currency
     *
     * @return created account
     */
    public Account openAccount(String description, Currency currency)
        {
        Account account = Account.create(this, description, currency);
        getAccountRepository().save(account);
        m_accountIds.add(account.getId());
        return account;
        }

    /**
     * Return all accounts for this customer.
     *
     * @return this customer's accounts
     */
    public Collection<Account> getAccounts()
        {
        return getAccountRepository().getAll(m_accountIds);
        }

    /**
     * Return specified customer's account.
     *
     * @param accountId  account id
     *
     * @return specified account, if owned by this customer
     * 
     * @throws SecurityException  if specified account is not owned by this customer
     */
    public Account getAccount(long accountId)
        {
        if (isOwnedAccount(accountId))
            {
            return getAccountRepository().getAccount(accountId);
            }
        else
            {
            throw new SecurityException("Shame on you! Access to other people's accounts is not allowed.");
            }
        }

    /**
     * Return whether specified account is owned by this customer.
     * <p/>
     * Coherence note: From a domain modeling perspective, it might be nicer to
     * have <tt>isOwnedBy(Customer)</tt> method on the Account instead. However,
     * that would require us to retrieve Account object from the cache (over
     * the network) in order to perform the check.
     * <p/>
     * This way, we can simply ask Customer object, which we already have
     * locally in the HTTP session. The end result is the same, but keep in
     * mind that we had to choose between the API cleanliness and performance,
     * and in this case we chose the latter.
     *
     * @param accountId  account to check for ownership
     *
     * @return true if the specified account is owned by this customer,
     *         false otherwise
     */
    public boolean isOwnedAccount(long accountId)
        {
        return m_accountIds.contains(accountId);
        }


    // ---- dependencies ----------------------------------------------------

    /**
     * Account repository.
     */
    private transient AccountRepository m_accountRepository;
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

    
    // ---- properties ------------------------------------------------------

    public Long getId()
        {
        return m_id;
        }

    public String getName()
        {
        return m_name;
        }

    public void setName(String name)
        {
        m_name = name;
        }

    public String getUsername()
        {
        return m_username;
        }

    public void setUsername(String username)
        {
        m_username = username;
        }

    public String getPassword()
        {
        return m_password;
        }

    public void setPassword(String password)
        {
        m_password = password;
        }

    public String getEmail()
        {
        return m_email;
        }

    public void setEmail(String email)
        {
        m_email = email;
        }

    public Address getAddress()
        {
        return m_address;
        }

    public void setAddress(Address address)
        {
        m_address = address;
        }


    // ---- AbstractEvolvable implementation --------------------------------

    public int getImplVersion()
        {
        return VERSION;
        }


    // ---- Versionable implementation --------------------------------------

    public Comparable getVersionIndicator()
        {
        return m_version;
        }

    public void incrementVersion()
        {
        ++m_version;
        }


    // ---- Object methods --------------------------------------------------

    public String toString()
        {
        return "Customer{" +
               "id=" + m_id +
               ", name='" + m_name + '\'' +
               ", username='" + m_username + '\'' +
               ", password='" + m_password + '\'' +
               ", email='" + m_email + '\'' +
               ", address=" + m_address +
               ", accountIds=" + m_accountIds +
               ", version=" + m_version +
               '}';
        }

    
    // ---- inner class: Serializer -----------------------------------------

    public static class Serializer
            extends AbstractPofSerializer<Customer>
        {
        protected void serializeAttributes(Customer obj, PofWriter writer)
                throws IOException
            {
            writer.writeLong      (0, obj.m_id);
            writer.writeString    (1, obj.m_name);
            writer.writeString    (2, obj.m_username);
            writer.writeString    (3, obj.m_password);
            writer.writeString    (4, obj.m_email);
            writer.writeObject    (5, obj.m_address);
            writer.writeCollection(6, obj.m_accountIds);
            writer.writeInt       (7, obj.m_version);
            }

        @SuppressWarnings({"unchecked"})
        protected Customer createInstance(PofReader reader)
                throws IOException
            {
            return new Customer(
                    reader.readLong(0),                              // id
                    reader.readString(1),                            // name
                    reader.readString(2),                            // username
                    reader.readString(3),                            // password
                    reader.readString(4),                            // email
                    (Address) reader.readObject(5),                  // address
                    reader.readCollection(6, new ArrayList<Long>()), // account ids
                    reader.readInt(7));                              // version
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
            SequenceGenerator.create("customer.id");


    // ---- data members ----------------------------------------------------

    /**
     * Customer ID.
     */
    private long m_id;

    /**
     * Customer name.
     */
    private String m_name;

    /**
     * Customer user name.
     */
    private String m_username;

    /**
     * Customer password.
     */
    private String m_password;

    /**
     * Customer email.
     */
    private String m_email;

    /**
     * Customer address.
     */
    private Address m_address;

    /**
     * Account identifiers.
     */
    private Collection<Long> m_accountIds;
    
    /**
     * Customer version (used for optimistic locking).
     */
    private int m_version;
    }
