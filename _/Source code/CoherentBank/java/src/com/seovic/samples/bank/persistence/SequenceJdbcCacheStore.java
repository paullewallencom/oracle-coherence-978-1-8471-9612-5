package com.seovic.samples.bank.persistence;


import com.seovic.identity.Sequence;
import com.seovic.coherence.util.persistence.AbstractJdbcCacheStore;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * DAO implementation for Sequence object
 *
 * @author Aleksandar Seovic  2010.02.02
 */
public class SequenceJdbcCacheStore
        extends AbstractJdbcCacheStore<Sequence>
    {
    /**
     * Constructor
     *
     * @param dataSource DataSource used for database connection
     */
    public SequenceJdbcCacheStore(DataSource dataSource)
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
    protected RowMapper<Sequence> getRowMapper()
        {
        return ROW_MAPPER;
        }

    /**
     * RowMapper implementation for Sequence
     */
    private static final RowMapper<Sequence> ROW_MAPPER =
            new RowMapper<Sequence>()
                {
                public Sequence mapRow(ResultSet rs, int i)
                        throws SQLException
                    {
                    return new Sequence(rs.getString("name"),
                                        rs.getLong("last_seq"));
                    }
                };

    private static final String MERGE_SQL =
            "MERGE INTO sequences (name, last_seq) " +
            "VALUES (:name, :last)";

    private static final String SELECT_SQL =
            "SELECT name, last_seq " +
            "FROM sequences WHERE name = ?";
    }