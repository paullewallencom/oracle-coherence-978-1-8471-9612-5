package com.seovic.samples.bank.persistence;


import com.seovic.coherence.util.persistence.AbstractJdbcCacheStore;

import com.seovic.samples.bank.domain.Transaction;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.TransactionType;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * DAO implementation for Transaction domain object
 *
 * @author pperalta Jan 30, 2010
 */
public class TransactionJdbcCacheStore
        extends AbstractJdbcCacheStore<Transaction>
    {
    /**
     * Constructor
     *
     * @param dataSource  JDBC datasource for underlying database
     */
    public TransactionJdbcCacheStore(DataSource dataSource)
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
    protected RowMapper<Transaction> getRowMapper()
        {
        return ROW_MAPPER;
        }

    /**
     * Overrides default implementation to handle composite key.
     *
     * @param key  transaction key
     *
     * @return primary key components for the given key
     */
    @Override
    protected Object[] getPrimaryKeyComponents(Object key)
        {
        Transaction.Id id = (Transaction.Id) key;
        return new Object[] {id.getAccountId(), id.getTransactionNumber()};
        }

    /**
     * RowMapper implementation for Account
     */
    private static final RowMapper<Transaction> ROW_MAPPER =
            new RowMapper<Transaction>()
                {
                public Transaction mapRow(ResultSet rs, int i)
                    throws SQLException
                    {
                    String currency = rs.getString("currency");

                    return new Transaction(
                            new Transaction.Id(rs.getLong("account_id"), rs.getLong("id")),
                            TransactionType.valueOf(rs.getString("type")),
                            new java.util.Date(rs.getTimestamp("time").getTime()),
                            rs.getString("description"),
                            new Money(rs.getBigDecimal("amount"), currency),
                            new Money(rs.getBigDecimal("balance"), currency));
                    }
                };

    private static final String MERGE_SQL =
            "INSERT INTO transactions (id, account_id, type, time, description, " +
                    "amount, balance, currency) " +
            "VALUES (:id.transactionNumber, :id.accountId, :type.name, " +
                    ":time, :description, :amount.amount, " +
                    ":balance.amount, :balance.currency.currencyCode)";

    private static final String SELECT_SQL =
            "SELECT account_id, id, type, time, description, amount, balance, currency " +
            "FROM transactions WHERE account_id = ? AND id = ?";
    }
