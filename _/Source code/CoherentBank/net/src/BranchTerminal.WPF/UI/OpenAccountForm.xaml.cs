using System.Windows;
using Seovic.Samples.Bank.Domain;
using Seovic.Samples.Bank.Support;

namespace Seovic.Samples.Bank.UI
{
    /// <summary>
    /// Interaction logic for OpenAccountForm.
    /// </summary>
    public partial class OpenAccountForm
    {
        private readonly Customer m_customer;

        public OpenAccountForm(Customer customer)
        {
            m_customer = customer;
            InitializeComponent();
        }

        public Customer Customer
        {
            get { return m_customer; }
        }

        void CloseForm(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
        }

        void SubmitForm(object sender, RoutedEventArgs e)
        {
            if (Validate())
            {
                Currency currency       = new Currency(lstCurrency.Text);
                Money    initialDeposit = new Money(txtAmount.Text, currency);
                string   description    = txtDescription.Text;

                Account account = Customer.OpenAccount(description, currency);
                account.Deposit(initialDeposit, "Initial deposit");

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