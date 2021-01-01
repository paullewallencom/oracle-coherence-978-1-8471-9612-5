package com.seovic.samples.bank.persistence;


import com.seovic.coherence.util.persistence.AbstractJdbcCacheStore;

import com.seovic.samples.bank.domain.Address;
import com.seovic.samples.bank.domain.Customer;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import java.util.List;


/**
 * DAO implementation for Customer domain object.
 *
 * @author pperalta Jan 30, 2010
 */
public class CustomerJdbcCacheStore
        extends AbstractJdbcCacheStore<Customer>
    {
    /**
     * Constructor
     *
     * @param dataSource DataSource used for database connection
     */
    public CustomerJdbcCacheStore(DataSource dataSource)
        {
        super(dataSource);
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMergeSql()
        {
        return MERGE_SQL;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSelectSql()
        {
        return SELECT_SQL;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected RowMapper<Customer> getRowMapper()
        {
        return m_customerMapper;
        }

    /**
     * RowMapper implementation for Customer
     */
    private class CustomerMapper
            implements RowMapper<Customer>
        {
        public Customer mapRow(ResultSet rs, int i)
                throws SQLException
            {
            // Obtain the list of accounts associated with this customer.
            // It is assumed that one customer will be loaded at a time -
            // this method of obtaining the customer accounts is susceptible
            // to the N+1 problem
            long customerId = rs.getLong("id");

            List<Long> accountIds = getJdbcTemplate().query(
                    ACCOUNTS_SQL, new SingleColumnRowMapper<Long>(), customerId);

            return new Customer(
                            customerId,
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            new Address(
                                    rs.getString("street"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getString("postal_code"),
                                    rs.getString("country")),
                            accountIds,
                            rs.getInt("version"));
            }
        }

    private final CustomerMapper m_customerMapper = new CustomerMapper();

    private static final String ACCOUNTS_SQL =
            "SELECT id from accounts WHERE customer_id = ?";

    private static final String MERGE_SQL =
            "MERGE INTO customers (id, name, username, password, email, " +
                    "street, city, state, postal_code, country, version) " +
            "VALUES (:id, :name, :username, :password, :email, " +
                    ":address.street, :address.city, :address.state, " +
                    ":address.postalCode, :address.country, :versionIndicator)";

    private static final String SELECT_SQL =
            "SELECT id, name, username, password, email, street, city, state, " +
                    "postal_code, country, version " +
            "FROM customers WHERE id = ?";
    }
