package com.seovic.samples.bank.domain;


import com.seovic.coherence.io.pof.AbstractPofSerializer;
import com.seovic.core.Entity;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.net.cache.KeyAssociation;

import java.util.Date;
import java.io.IOException;
import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * @author Aleksandar Seovic  2009.12.01
 */
public class Transaction
        implements Entity<Transaction.Id>
    {
    // ---- constructors and factory methods --------------------------------

    public Transaction(Id id, TransactionType type,
                       Date time, String description, Money amount,
                       Money balance)
        {
        m_id          = id;
        m_type        = type;
        m_time        = time;
        m_description = description;
        m_amount      = amount;
        m_balance     = balance;
        }

    static Transaction create(long accountId, long transactionNumber,
                              TransactionType type, String description,
                              Money amount, Money balance)
        {
        return new Transaction(new Id(accountId, transactionNumber), type,
                               new Date(), description, amount, balance);
        }


    // ---- properties ------------------------------------------------------

    public Id getId()
        {
        return m_id;
        }

    public TransactionType getType()
        {
        return m_type;
        }

    public Date getTime()
        {
        return m_time;
        }

    public String getDescription()
        {
        return m_description;
        }

    public Money getAmount()
        {
        return m_amount;
        }

    public Money getBalance()
        {
        return m_balance;
        }


    // ---- Object methods --------------------------------------------------

    public String toString()
        {
        return "Transaction{" +
               "id=" + m_id +
               ", type=" + m_type +
               ", time=" + m_time +
               ", description='" + m_description + '\'' +
               ", amount=" + m_amount +
               ", balance=" + m_balance +
               '}';
        }


    // ---- inner class: Id -------------------------------------------------

    public static class Id
            implements PortableObject, KeyAssociation
        {
        // ---- data members --------------------------------------------

        public Id()
            {
            }

        public Id(long accountId, long transactionNumber)
            {
            m_accountId         = accountId;
            m_transactionNumber = transactionNumber;
            }

        @JsonIgnore
        public Object getAssociatedKey()
            {
            return m_accountId;
            }

        public long getAccountId()
            {
            return m_accountId;
            }

        public long getTransactionNumber()
            {
            return m_transactionNumber;
            }

        public void readExternal(PofReader reader)
                throws IOException
            {
            m_accountId         = reader.readLong(0);
            m_transactionNumber = reader.readLong(1);
            }

        public void writeExternal(PofWriter writer)
                throws IOException
            {
            writer.writeLong(0, m_accountId);
            writer.writeLong(1, m_transactionNumber);
            }

        @Override
        public boolean equals(Object o)
            {
            if (this == o)
                {
                return true;
                }
            if (o == null || getClass() != o.getClass())
                {
                return false;
                }

            Id id = (Id) o;

            return m_accountId == id.m_accountId
                   && m_transactionNumber == id.m_transactionNumber;
            }

        @Override
        public int hashCode()
            {
            int result = (int) (m_accountId ^ (m_accountId >>> 32));
            result = 31 * result + (int) (m_transactionNumber ^ (
                    m_transactionNumber >>> 32));
            return result;
            }

        @Override
        public String toString()
            {
            return "Id(" +
                   "accountId=" + m_accountId +
                   ", transactionNumber=" + m_transactionNumber +
                   ')';
            }

        // ---- data members --------------------------------------------

        private long m_accountId;

        private long m_transactionNumber;
        }


    // ---- inner class: Serializer -----------------------------------------

    public static class Serializer
            extends AbstractPofSerializer<Transaction>
        {
        protected void serializeAttributes(Transaction transaction, PofWriter pofWriter)
                throws IOException
            {
            pofWriter.writeObject(0, transaction.m_id);
            pofWriter.writeObject(1, transaction.m_type);
            pofWriter.writeDate  (2, transaction.m_time);
            pofWriter.writeString(3, transaction.m_description);
            pofWriter.writeObject(4, transaction.m_amount);
            pofWriter.writeObject(5, transaction.m_balance);
            }

        protected Transaction createInstance(PofReader pofReader)
                throws IOException
            {
            return new Transaction(
                    (Id) pofReader.readObject(0),
                    (TransactionType) pofReader.readObject(1),
                    pofReader.readDate(2),
                    pofReader.readString(3),
                    (Money) pofReader.readObject(4),
                    (Money) pofReader.readObject(5));
            }
        }


    // ---- data members ----------------------------------------------------

    private final Id m_id;
    private final TransactionType m_type;
    private final Date m_time;
    private final String m_description;
    private final Money m_amount;
    private final Money m_balance;
    }
