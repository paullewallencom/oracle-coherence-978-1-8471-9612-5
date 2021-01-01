using Seovic.Core;

namespace Seovic.Samples.Bank.Domain
{
    public interface ICommandFactory
    {
        ICommand<Account> CreateOpenAccountCommand(long customerId, string decription, Currency currency);

        ICommand<Transaction> CreateWithdrawalCommand(long accountId, Money amount, string description);
        ICommand<Transaction> CreateDepositCommand(long accountId, Money amount, string description);
    }
}