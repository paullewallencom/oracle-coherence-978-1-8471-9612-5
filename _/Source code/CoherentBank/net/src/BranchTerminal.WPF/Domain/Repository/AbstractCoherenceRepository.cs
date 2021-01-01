using System.Collections;
using System.Collections.Generic;
using System.Linq;

using Seovic.Core;

using Tangosol.Net;
using Tangosol.Net.Cache;
using Tangosol.Util;

namespace Seovic.Samples.Bank.Domain.Repository
{
    /// <summary>
    /// Base class for Coherence repository implementations.
    /// </summary>
    /// <typeparam name="TKey"></typeparam>
    /// <typeparam name="TValue"></typeparam>
    /// <author>Ivan Cikic  2009.12.07</author>
    public abstract class AbstractCoherenceRepository<TKey, TValue> where TValue : IEntity<TKey>
    {
        protected abstract INamedCache Cache
        {
            get;
        }

        public TValue this[TKey key]
        {
            get { return (TValue) Cache[key]; }
        }

        public IEnumerable<TValue> GetAll(ICollection<TKey> keys)
        {
            // need wrapping with List<T> to overcome generic collections
            List<TKey> wrapped = new List<TKey>(keys);
            return Wrap(Cache.GetAll(wrapped).Values);
        }

        public void Save(TValue value)
        {
            Cache.Insert(value.Id, value);
        }

        protected object Invoke(TKey key, IEntryProcessor processor)
        {
            return Cache.Invoke(key, processor);    
        }

        protected TValue QueryForSingleValue(IFilter filter)
        {
            ICollection entries = Cache.Entries;

            return entries.Count == 0
                       ? default(TValue)
                       : entries.Cast<TValue>().First();
        }

        protected IEnumerable<TValue> QueryForValues(IFilter filter)
        {
            return Wrap(Cache.GetValues(filter));
        }

        protected IEnumerable<TValue> QueryForValues(IFilter filter, IComparer comparer)
        {
            return Wrap(Cache.GetValues(filter, comparer));
        }

        private static IEnumerable<TValue> Wrap(IEnumerable source)
        {
            return source.Cast<TValue>();
        }
    }
}