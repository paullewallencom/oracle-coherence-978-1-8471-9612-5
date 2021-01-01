using Seovic.Samples.Bank.Domain.Commands;
using Seovic.Samples.Bank.Domain.Repository;

namespace Seovic.Samples.Bank.Domain
{
    public class DependencyRegistry
    {
        protected DependencyRegistry()
        {
            m_customerRepository = new CoherenceCustomerRepository();
            m_commandFactory     = new CoherenceCommandFactory();
        }

        public static ICustomerRepository CustomerRepository
        {
            get
            {
                return Instance.m_customerRepository;
            }
        }

        public static ICommandFactory CommandFactory
        {
            get
            {
                return Instance.m_commandFactory;
            }
        }

        private static readonly DependencyRegistry Instance = new DependencyRegistry();

        private readonly ICustomerRepository m_customerRepository;
        private readonly ICommandFactory     m_commandFactory;
    }
}