using System;
using System.Collections.ObjectModel;
using System.Windows.Threading;
using Seovic.Core.Extractor;
using Seovic.Samples.Bank.Domain;
using Seovic.Samples.Bank.Support;

using Tangosol.Net;
using Tangosol.Net.Cache;
using Tangosol.Util;
using Tangosol.Util.Filter;

namespace Seovic.Samples.Bank.UI
{
    public class Transactions : ObservableCollection<Transaction>, IDisposable
    {
        private ContinuousQueryCache m_view;
        private readonly WpfCacheListener m_listener;
        private readonly INamedCache m_transactions;

        public Transactions(DispatcherObject control)
        {
            m_listener = new WpfCacheListener(control);
            m_listener.EntryDeleted += OnDelete;
            m_listener.EntryInserted += OnAdd;
            m_listener.EntryUpdated += OnUpdate;

            m_transactions = CacheFactory.GetCache("transactions");
        }

        public void ApplyFilter(long accountId, DateTime from, DateTime to)
        {
            IFilter accountFilter = new EqualsFilter(new SpelExtractor("Id.AccountId"), accountId);
            IFilter periodFilter  = new BetweenFilter(new PropertyExtractor("Time"), from, to);
            IFilter filter        = new KeyAssociatedFilter(
                new AndFilter(accountFilter, periodFilter), accountId);

            if (m_view != null)
            {
                m_view.RemoveCacheListener(m_listener);
                m_view.Release();
                Clear();
            }
            m_view = new ContinuousQueryCache(m_transactions, filter, m_listener);
        }

        public void OnAdd(object sender, CacheEventArgs evt)
        {
            Add((Transaction) evt.NewValue);
        }

        public void OnUpdate(object sender, CacheEventArgs evt)
        {
            int index = IndexOf((Transaction) evt.OldValue);
            SetItem(index, (Transaction) evt.NewValue);
        }

        public void OnDelete(object sender, CacheEventArgs evt)
        {
            Remove((Transaction) evt.OldValue);
        }

        public void Dispose()
        {
            m_view.Release();
        }
    }
}