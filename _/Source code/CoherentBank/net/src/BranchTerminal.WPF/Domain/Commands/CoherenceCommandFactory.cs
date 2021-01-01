using System;
using Seovic.Coherence.Util;
using Seovic.Core;
using Tangosol.IO.Pof;
using Tangosol.Net;
using Tangosol.Net.Cache;
using Tangosol.Util.Processor;

namespace Seovic.Samples.Bank.Domain.Commands
{
    public class CoherenceCommandFactory : ICommandFactory
    {
        private static readonly IInvocableCache s_customers = CacheFactory.GetCache("customers");
        private static readonly IInvocableCache s_accounts  = CacheFactory.GetCache("accounts");

        #region ICommandFactory implementation

        public ICommand<Account> CreateOpenAccountCommand(long customerId, string description, Currency currency)
        {
            return new EntryProcessorCommand<Account>(
                s_customers, customerId, new OpenAccountProcessor(description, currency));
        }

        public ICommand<Transaction> CreateWithdrawalCommand(long accountId, Money amount, string description)
        {
            return new EntryProcessorCommand<Transaction>(
                s_accounts, accountId, new WithdrawalProcessor(amount, description));
        }

        public ICommand<Transaction> CreateDepositCommand(long accountId, Money amount, string description)
        {
            return new EntryProcessorCommand<Transaction>(
                s_accounts, accountId, new DepositProcessor(amount, description));
        }

        #endregion

        #region Inner class: OpenAccountProcessor

        public class OpenAccountProcessor : AbstractProcessor, IPortableObject
        {
            #region Constructors

            public OpenAccountProcessor()
            {
            }

            public OpenAccountProcessor(string description, Currency currency)
            {
                m_description = description;
                m_currency    = currency;
            }

            #endregion

            #region IEntryProcessor implementation

            public override object Process(IInvocableCacheEntry entry)
            {
                throw new NotSupportedException();
            }

            #endregion

            #region IPortableObject implementation

            public void ReadExternal(IPofReader reader)
            {
                m_description = reader.ReadString(0);
                m_currency    = (Currency) reader.ReadObject(1);
            }

            public void WriteExternal(IPofWriter writer)
            {
                writer.WriteString(0, m_description);
                writer.WriteObject(1, m_currency);
            }

            #endregion

            #region Data members

            private string   m_description;
            private Currency m_currency;

            #endregion
        }

        #endregion

        #region Inner class: AccountProcessor

        public abstract class AccountProcessor : AbstractProcessor, IPortableObject
        {
            #region Constructors

            protected AccountProcessor()
            {
            }

            protected AccountProcessor(Money amount, string description)
            {
                m_amount = amount;
                m_description = description;
            }

            #endregion

            #region IEntryProcessor implementation

            public override object Process(IInvocableCacheEntry entry)
            {
                throw new NotSupportedException();
            }

            #endregion

            #region IPortableObject implementation

            public void ReadExternal(IPofReader reader)
            {
                m_amount = (Money)reader.ReadObject(0);
                m_description = reader.ReadString(1);
            }

            public void WriteExternal(IPofWriter writer)
            {
                writer.WriteObject(0, m_amount);
                writer.WriteString(1, m_description);
            }

            #endregion

            #region Data members

            protected Money m_amount;
            protected string m_description;

            #endregion
        }

        #endregion

        #region Inner class: WithdrawalProcessor

        public class WithdrawalProcessor : AccountProcessor
        {
            public WithdrawalProcessor()
            {
            }

            public WithdrawalProcessor(Money amount, string description)
                : base(amount, description)
            {
            }
        }

        #endregion

        #region Inner class: DepositProcessor

        public class DepositProcessor : AccountProcessor
        {
            public DepositProcessor()
            {
            }

            public DepositProcessor(Money amount, string description)
                : base(amount, description)
            {
            }
        }

        #endregion

    }
}