using Seovic.Samples.Bank.Domain;

namespace Seovic.Samples.Bank.UI
{
    public class PostDepositForm : PostTransactionForm
    {
        public PostDepositForm(Account account) : base(account)
        {
            Title = "Post Deposit";
        }

        protected override void PostTransaction(Money amount, string description)
        {
            Account.Deposit(amount, description);            
        }
    }
}