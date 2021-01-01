using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows.Threading;

using Seovic.Samples.Bank.Domain;
using Seovic.Samples.Bank.Support;

using Tangosol.Net;
using Tangosol.Net.Cache;
using Tangosol.Util.Filter;

namespace Seovic.Samples.Bank.UI
{
    public class Accounts : ObservableCollection<Account>, IDisposable
    {
        private readonly ContinuousQueryCache m_view;
        private readonly IDictionary<long, Account> m_accountMap;

        public Accounts(DispatcherObject control)
        {
            m_accountMap = new Dictionary<long, Account>();

            WpfCacheListener listener = new WpfCacheListener(control);
            listener.EntryDeleted  += OnDelete;
            listener.EntryInserted += OnAdd;
            listener.EntryUpdated  += OnUpdate;

            INamedCache accounts = CacheFactory.GetCache("accounts");
            m_view = new ContinuousQueryCache(accounts, AlwaysFilter.Instance, listener);
        }

        public void OnAdd(object sender, CacheEventArgs evt)
        {
            Account account = (Account) evt.NewValue;
            Add(account);
            m_accountMap.Add(account.Id, account);
        }

        public void OnUpdate(object sender, CacheEventArgs evt)
        {
            Account account = (Account) evt.NewValue;
            m_accountMap[account.Id].Balance = account.Balance;
        }

        public void OnDelete(object sender, CacheEventArgs evt)
        {
            Account account = (Account) evt.OldValue;
            Remove(account);
            m_accountMap.Remove(account.Id);
        }

        public void Dispose()
        {
            m_view.Release();
        }
    }
}