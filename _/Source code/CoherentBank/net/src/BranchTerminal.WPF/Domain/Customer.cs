using System;
using System.Collections.Generic;

using Seovic.Core;
using Seovic.Identity;
using Tangosol.IO.Pof;
using Tangosol.Util;

namespace Seovic.Samples.Bank.Domain
{
    public class Customer : IEntity<long>, IPortableObject, IVersionable
    {
        #region Constructors and factory methods

        public Customer()
        {}

        private Customer(long id, string name, string username, string password,
                     string email, Address address, ICollection<long> accountIds,
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

        public static Customer Create(string name, string username, string password, string email)
        {
            return new Customer(IdGen.GenerateIdentity(), name, username, password,
                                email, new Address(), new List<long>(), 1);
        }

        #endregion

        #region Properties

        public long Id
        {
            get { return m_id; }
        }

        public string Name
        {
            get { return m_name; }
        }

        public string Username
        {
            get { return m_username; }
        }

        public string Email
        {
            get { return m_email; }
        }

        public Address Address
        {
            get { return m_address; }
        }

        public int Version
        {
            get { return m_version; }
        }

        #endregion

        #region Public methods

        public Account OpenAccount(String description, Currency currency)
        {
            ICommand<Account> command =
                CommandFactory.CreateOpenAccountCommand(Id, description, currency);
            Account account = command.Execute();
            m_accountIds.Add(account.Id);
            return account;
        }

        #endregion

        #region Dependencies

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

        #region IVersionable implementation

        public void IncrementVersion()
        {
            ++m_version;
        }

        public IComparable VersionIndicator
        {
            get { return m_version; }
        }

        #endregion

        #region Object methods

        public override string ToString()
        {
            return string.Format("Id: {0}, Name: {1}," + 
                                 " Username: {2}, Password: {3}, Email: {4}, Address: {5}," + 
                                 " AccountIds: {6}, Version: {7}", 
                                 m_id, m_name, m_username, m_password, 
                                 m_email, m_address, m_accountIds, m_version);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_id         = reader.ReadInt64(0);
            m_name       = reader.ReadString(1);
            m_username   = reader.ReadString(2);
            m_password   = reader.ReadString(3);
            m_email      = reader.ReadString(4);
            m_address    = (Address) reader.ReadObject(5);
            m_accountIds = reader.ReadCollection<long>(6, new List<long>());
            m_version    = reader.ReadInt32(7);          
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteInt64     (0, m_id);
            writer.WriteString    (1, m_name);
            writer.WriteString    (2, m_username);
            writer.WriteString    (3, m_password);
            writer.WriteString    (4, m_email);
            writer.WriteObject    (5, m_address);
            writer.WriteCollection(6, m_accountIds);
            writer.WriteInt32     (7, m_version);
        }

        #endregion

        #region Data Members

        private static readonly SequenceGenerator IdGen =
            SequenceGenerator.Create("customer.id");

        private long m_id;
        private string m_name;
        private string m_username;
        private string m_password;
        private string m_email;
        private Address m_address;
        private ICollection<long> m_accountIds;
        private int m_version;

        #endregion
    }
}