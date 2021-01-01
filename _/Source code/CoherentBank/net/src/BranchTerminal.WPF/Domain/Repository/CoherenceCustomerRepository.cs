using Tangosol.Net;

namespace Seovic.Samples.Bank.Domain.Repository
{
    /// <summary>
    /// Coherence backed customer repository.
    /// </summary>
    /// <author>Ivan Cikic  2009.12.07</author>
    public class CoherenceCustomerRepository 
        : AbstractCoherenceRepository<long, Customer>, ICustomerRepository
    {
        #region ICustomerRepository implementation

        protected override INamedCache Cache
        {
            get { return Customers; }
        }

        public Customer GetCustomer(long id)
        {
            return this[id];
        }

        #endregion

        private static readonly INamedCache Customers = 
            CacheFactory.GetCache("customers");
    }
}