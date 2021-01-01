using System;

using Seovic.Core;

using Tangosol.IO.Pof;
using Tangosol.Net.Cache;

namespace Seovic.Samples.Bank.Domain
{
    public class Transaction : IEntity<Transaction.TransactionId>, IPortableObject
    {
        #region Constructors and factory methods

        public Transaction()
        {}

        private Transaction(TransactionId id, TransactionType type,
                            DateTime time, String description, 
                            Money amount, Money balance)
        {
            this.id          = id;
            this.type        = type;
            this.time        = time;
            this.description = description;
            this.amount      = amount;
            this.balance     = balance;
        }

        public static Transaction Create(long accountId, long transactionNumber,
                                         TransactionType type, String description, 
                                         Money amount, Money balance)
        {
            return new Transaction(new TransactionId(accountId, transactionNumber), type,
                                   DateTime.Now, description, amount, balance);
        }

        #endregion

        #region Properties

        public TransactionId Id
        {
            get { return id; }
        }

        public TransactionType Type
        {
            get { return type; }
        }

        public DateTime Time
        {
            get { return time; }
        }

        public string Description
        {
            get { return description; }
        }

        public Money Amount
        {
            get { return amount; }
        }

        public Money Balance
        {
            get { return balance; }
        }

        public Money DepositAmount
        {
            get
            {
                return TransactionType.DEPOSIT.Equals(Type) 
                    ? Amount
                    : null;
            }
        }

        public Money WithdrawalAmount
        {
            get
            {
                return TransactionType.WITHDRAWAL.Equals(Type) 
                    ? Amount
                    : null;
            }
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Returns a string representation of the current object.
        /// </summary>
        /// <returns>
        /// String that represents the current object.
        /// </returns>
        public override string ToString()
        {
            return string.Format("Id: {0}, Type: {1}, Time: {2}, Description: {3}," + 
                " Amount: {4}, Balance: {5}", id, type, time, description, amount, balance);
        }

        #endregion

        #region IPortableObject implementation

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, id);
            writer.WriteObject(1, type);
            writer.WriteDate  (2, time);
            writer.WriteString(3, description);
            writer.WriteObject(4, amount);
            writer.WriteObject(5, balance);
        }

        public void ReadExternal(IPofReader reader)
        {
            id          = (TransactionId) reader.ReadObject(0);
            type        = (TransactionType) reader.ReadObject(1);
            time        = reader.ReadDate(2);
            description = reader.ReadString(3);
            amount      = (Money) reader.ReadObject(4);
            balance     = (Money) reader.ReadObject(5);
        }


        #endregion

        #region Inner class: TransactionId

        public class TransactionId : IKeyAssociation, IPortableObject
        {
            #region Constructors

            public TransactionId()
            {
            }

            public TransactionId(long accountId, long transactionNumber)
            {
                this.accountId         = accountId;
                this.transactionNumber = transactionNumber;
            }

            #endregion

            #region Properties

            public long AccountId
            {
                get { return accountId; }
            }

            public long TransactionNumber
            {
                get { return transactionNumber; }
            }

            #endregion

            #region IKeyAssociation implementation

            public object AssociatedKey
            {
                get { return accountId; }
            }

            #endregion

            #region IPortableObject implementation

            public void ReadExternal(IPofReader reader)
            {
                accountId         = reader.ReadInt64(0);
                transactionNumber = reader.ReadInt64(1);
            }

            public void WriteExternal(IPofWriter writer)
            {
                writer.WriteInt64(0, accountId);
                writer.WriteInt64(1, transactionNumber);
            }

            #endregion

            #region Data members

            private long accountId;
            private long transactionNumber;

            #endregion
        }

        #endregion

        #region Data members

        private TransactionId id;
        private TransactionType type;
        private DateTime time;
        private String description;
        private Money amount;
        private Money balance;

        #endregion
    }
}