using System;

namespace Seovic.Samples.Bank.Domain
{
    public class InsufficientFundsException : Exception
    {
        public InsufficientFundsException(Money balance, Money withdrawalAmount)
            : base("Attempted to withdraw " + withdrawalAmount
                  + ". Available balance is " + balance)
        {
        }
    }
}