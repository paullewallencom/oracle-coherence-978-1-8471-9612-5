namespace Seovic.Samples.Bank.Domain
{
    public interface ICustomerRepository
    {
        Customer GetCustomer(long id);
    }
}
