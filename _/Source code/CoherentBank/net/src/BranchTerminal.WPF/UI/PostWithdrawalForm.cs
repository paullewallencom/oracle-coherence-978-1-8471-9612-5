using System;
using System.Windows;
using Seovic.Samples.Bank.Domain;

namespace Seovic.Samples.Bank.UI
{
    public class PostWithdrawalForm : PostTransactionForm
    {
        public PostWithdrawalForm(Account account)
            : base(account)
        {
            Title = "Post Withdrawal";
        }

        protected override void PostTransaction(Money amount, string description)
        {
            try
            {
                Account.Withdraw(amount, description);
            }
            catch (InsufficientFundsException e)
            {
                MessageBox.Show(e.Message, "Insufficient Funds", 
                                MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}