package com.seovic.samples.bank.persistence;


import com.seovic.coherence.util.persistence.AbstractJdbcCacheStore;

import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Money;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * DAO implementation for Account domain object
 *
 * @author pperalta Jan 30, 2010
 */
public class AccountJdbcCacheStore
        extends AbstractJdbcCacheStore<Account>
    {
    /**
     * Constructor
     *
     * @param dataSource DataSource used for database connection
     */
    public AccountJdbcCacheStore(DataSource dataSource)
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
    protected RowMapper<Account> getRowMapper()
        {
        return ROW_MAPPER;
        }

    /**
     * RowMapper implementation for Account
     */
    private static final RowMapper<Account> ROW_MAPPER =
            new RowMapper<Account>()
                {
                public Account mapRow(ResultSet rs, int i)
                        throws SQLException
                    {
                    return new Account(
                            rs.getLong("id"),
                            rs.getString("description"),
                            new Money(rs.getLong("balance"), rs.getString("currency")),
                            rs.getLong("last_tx"),
                            rs.getLong("customer_id"));
                    }
                };

    private static final String MERGE_SQL =
            "MERGE INTO accounts (id, description, balance, currency, last_tx, customer_id) " +
            "VALUES (:id, :description, :balance.amount, :balance.currency.currencyCode, :lastTransactionId, :customerId)";

    private static final String SELECT_SQL =
            "SELECT id, description, balance, currency, last_tx, customer_id " +
            "FROM accounts WHERE id = ?";
    }
