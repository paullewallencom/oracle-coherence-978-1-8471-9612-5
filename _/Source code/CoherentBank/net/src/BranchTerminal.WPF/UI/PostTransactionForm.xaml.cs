using System.Windows;

using Seovic.Samples.Bank.Domain;
using Seovic.Samples.Bank.Support;

namespace Seovic.Samples.Bank.UI
{
    /// <summary>
    /// Interaction logic for PostTransactionForm.
    /// </summary>
    public abstract partial class PostTransactionForm
    {
        protected PostTransactionForm(Account account)
        {
            Account = account;
            InitializeComponent();
            lstCurrency.Text = account.Currency.CurrencyCode;
            txtDescription.Focus();
        }

        public Account Account { get; set; }

        protected abstract void PostTransaction(Money amount, string description);

        void CloseForm(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
        }

        void SubmitForm(object sender, RoutedEventArgs e)
        {
            if (Validate())
            {
                PostTransaction(new Money(txtAmount.Text, lstCurrency.Text), txtDescription.Text);
                DialogResult = true;
            }
        }

        protected virtual bool Validate()
        {
            bool isValid = true;

            if (string.IsNullOrEmpty(txtDescription.Text))
            {
                txtDescription.MarkInvalid("Description is a required field");
                isValid = false;
            }

            double amount;
            if (string.IsNullOrEmpty(txtAmount.Text))
            {
                txtAmount.MarkInvalid("Amount is a required field");
                isValid = false;
            }
            else if (!double.TryParse(txtAmount.Text, out amount) || amount <= 0)
            {
                txtAmount.MarkInvalid("Amount must be a valid positive number");
                isValid = false;
            }

            if (string.IsNullOrEmpty(lstCurrency.Text))
            {
                lstCurrency.MarkInvalid("Currency is a required field");
                isValid = false;
            }

            return isValid;
        }
    }
}