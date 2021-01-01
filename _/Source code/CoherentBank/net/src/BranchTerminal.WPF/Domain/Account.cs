using System;
using System.ComponentModel;

using Seovic.Core;
using Seovic.Identity;

using Tangosol.IO;
using Tangosol.IO.Pof;

namespace Seovic.Samples.Bank.Domain
{
    public class Account : 
        AbstractEvolvable, IEntity<long>, IPortableObject,
        INotifyPropertyChanged
    {
        #region Constructors and factory methods

        public Account()
        {}

        private Account(long id, string description, Money balance, long customerId)
        {
            m_id                = id;
            m_description       = description;
            m_balance           = balance;
            m_customerId        = customerId;
        }

        public static Account Create(Customer customer, String description, Currency currency)
        {
            Account account = new Account(IdGen.GenerateIdentity(), description,
                                          new Money(0, currency), customer.Id);
            
            account.m_customer = customer;
            return account;
        }

        #endregion

        #region Properties

        public long Id
        {
            get { return m_id; }
        }

        public string Description
        {
            get { return m_description; }
        }

        public Money Balance
        {
            get { return m_balance; }
            set
            {
                m_balance = value;
                OnPropertyChanged("Balance");
            }
        }

        public Currency Currency
        {
            get { return m_balance.Currency; }
        }
        public Customer Customer
        {
            get
            {
                Customer customer = m_customer;
                if (customer == null)
                {
                    m_customer = customer = CustomerRepository.GetCustomer(m_customerId);
                }
                return customer;
            }
        }

        #endregion

        #region AbstractEvolvable implementation

        public override int ImplVersion
        {
            get { return VERSION; }
        }

        #endregion

        #region Public Methods

        public void Deposit(Money amount, string description)
        {
            ICommand<Transaction> command = 
                CommandFactory.CreateDepositCommand(Id, amount, description);
            Transaction tx = command.Execute();
            m_balance = tx.Balance;
        }

        public void Withdraw(Money amount, string description)
        {
            try
            {
                ICommand<Transaction> command = 
                    CommandFactory.CreateWithdrawalCommand(Id, amount, description);
                Transaction tx = command.Execute();
                m_balance = tx.Balance;
            }
            catch (Exception)
            {
                throw new InsufficientFundsException(Balance, amount);
            }
        }

        #endregion

        #region Dependencies

        [NonSerialized]
        private ICustomerRepository _customerRepository;

        public ICustomerRepository CustomerRepository
        {
            get
            {
                if (_customerRepository == null)
                {
                    _customerRepository = DependencyRegistry.CustomerRepository;
                }
                return _customerRepository;

            }
            set { _customerRepository = value; }
        }

        [NonSerialized]
        private ICommandFactory _commandFactory;

        public ICommandFactory CommandFactory
        {
            get
            {
                if (_commandFactory == null)
                {
                    _commandFactory = DependencyRegistry.CommandFactory;
                }
                return _commandFactory;
            }
            set { _commandFactory = value; }
        }

        #endregion

        #region Implementation of INotifyPropertyChanged

        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged(string name)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(name));
            }
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_id          = reader.ReadInt64(0);
            m_description = reader.ReadString(1);
            m_balance     = (Money) reader.ReadObject(2);
            // 3 - lastTransactionId, not needed on the client
            m_customerId  = reader.ReadInt64(4);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteInt64 (0, m_id);
            writer.WriteString(1, m_description);
            writer.WriteObject(2, m_balance);
            // 3 - lastTransactionId, not present on the client
            writer.WriteInt64 (4, m_customerId);
        }

        #endregion

        #region Object methods overrides

        public bool Equals(Account obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return obj.m_id == m_id &&
                   Equals(obj.m_description, m_description) &&
                   Equals(obj.m_balance, m_balance) &&
                   obj.m_customerId == m_customerId;
        }

        /// <summary>
        /// Determines whether the specified <see cref="T:System.Object" /> is equal to the current <see cref="T:System.Object" />.
        /// </summary>
        /// <returns>
        /// true if the specified <see cref="T:System.Object" /> is equal to the current <see cref="T:System.Object" />; otherwise, false.
        /// </returns>
        /// <param name="obj">
        /// The <see cref="T:System.Object" /> to compare with the current <see cref="T:System.Object" />. 
        /// </param>
        /// <exception cref="T:System.NullReferenceException">
        /// The <paramref name="obj" /> parameter is null.
        /// </exception><filterpriority>2</filterpriority>
        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (Account)) return false;
            return Equals((Account) obj);
        }

        /// <summary>
        /// Serves as a hash function for a particular type. 
        /// </summary>
        /// <returns>
        /// A hash code for the current <see cref="T:System.Object" />.
        /// </returns>
        /// <filterpriority>2</filterpriority>
        public override int GetHashCode()
        {
            unchecked
            {
                int result = 0;
                result = (result*397) ^ m_id.GetHashCode();
                result = (result*397) ^ (m_description != null ? m_description.GetHashCode() : 0);
                result = (result*397) ^ (m_balance != null ? m_balance.GetHashCode() : 0);
                result = (result*397) ^ m_customerId.GetHashCode();
                return result;
            }
        }

        public override string ToString()
        {
            return String.Format("Account{{id={0}" +
                                 ", description='{1}'" +
                                 ", balance={2}" +
                                 ", customerId={3}" +
                                 "}", m_id, m_description, m_balance, m_customerId);
        }

        #endregion

        #region Static members
    
        /// <summary>
        /// Class version (for evolution support).
        /// </summary>
        public static readonly int VERSION = 1;

        /// <summary>
        /// Identity generator.
        /// </summary>
        private static readonly SequenceGenerator IdGen =
            SequenceGenerator.Create("account.id");

        #endregion

        #region Data Members

        private long   m_id;
        private string m_description;
        private Money  m_balance;
        private long   m_customerId;

        [NonSerialized]
        private Customer m_customer;

        #endregion
    }
}