package com.seovic.samples.bank.domain.persistence;


import org.junit.BeforeClass;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import com.seovic.io.ClasspathResourceReader;


/**
 * Base class for JDBC DAO tests.
 *
 * @author pperalta Jan 30, 2010
 */
public class AbstractJdbcCacheStoreTests
    {
    /**
     * Creates an in-memory H2 database for testing.
     *
     * @throws Exception  if an error occurs
     */
    @BeforeClass
    public static void setupDatabase()
            throws Exception
        {
        m_dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
        RunScript.execute(m_dataSource.getConnection(),
                          new ClasspathResourceReader("create-tables.sql"));
        }

    protected DataSource getDataSource()
        {
        return m_dataSource;
        }

    private static DataSource m_dataSource;
    }
