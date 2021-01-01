using System;
using System.Windows;
using System.Windows.Controls;

using Seovic.Samples.Bank.Domain;

namespace Seovic.Samples.Bank.UI
{
    /// <summary>
    /// Interaction logic for AccountsWindow.xaml
    /// </summary>
    public partial class AccountsWindow
    {
        private readonly Accounts m_accounts;
        private readonly Transactions m_transactions;

        public AccountsWindow()
        {
            try
            {
                m_accounts     = new Accounts(this);
                m_transactions = new Transactions(this);

                InitializeComponent();
            }
            catch (Exception ex)
            {
                string message = string.Format("Error occured during initialization. " +
                        "Make sure that Oracle Coherence proxy server is running. \n" +
                        "Exception caught: {0}", ex.Message);
                MessageBox.Show(message);
                throw;
            }
        }

        public Accounts Accounts
        {
            get { return m_accounts; }
        }

        public Transactions Transactions
        {
            get { return m_transactions; }
        }

        public Account SelectedAccount
        {
            get { return (Account) AccountList.SelectedItem; }
        }

        private void PostDeposit(object sender, RoutedEventArgs e)
        {
            PostDepositForm form = new PostDepositForm(SelectedAccount);
            form.ShowDialog();
        }

        private void PostWithdrawal(object sender, RoutedEventArgs e)
        {
            PostWithdrawalForm form = new PostWithdrawalForm(SelectedAccount);
            form.ShowDialog();
        }

        private void OpenAccount(object sender, RoutedEventArgs e)
        {
            OpenAccountForm form = new OpenAccountForm(SelectedAccount.Customer);
            form.ShowDialog();
        }

        private void ApplyTransactionFilter(object sender, RoutedEventArgs e)
        {
            RefreshTransactions();
        }

        private void RefreshTransactions()
        {
            DateTime from = 
                dtStartDate.SelectedDate.HasValue
                    ? dtStartDate.SelectedDate.Value
                    : DateTime.Today.AddMonths(-1);
            DateTime to = 
                dtEndDate.SelectedDate.HasValue
                    ? dtEndDate.SelectedDate.Value
                    : DateTime.Today;

            dtStartDate.SelectedDate = from;
            dtEndDate.SelectedDate   = to;

            Transactions.ApplyFilter(SelectedAccount.Id, from, to.AddDays(1));
        }

        private void AccountList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Account account = SelectedAccount;

            if (account != null)
            {
                RefreshTransactions();
                btnOpenAccount.IsEnabled = true;
                btnDeposit.IsEnabled = true;
                btnWithdraw.IsEnabled = true;
            }
            else 
            {
                btnOpenAccount.IsEnabled = false;
                btnDeposit.IsEnabled = false;
                btnWithdraw.IsEnabled = false;
            }
        }

        private void Window_Unloaded(object sender, RoutedEventArgs e)
        {
            m_accounts.Dispose();
            m_transactions.Dispose();
        }
    }
}