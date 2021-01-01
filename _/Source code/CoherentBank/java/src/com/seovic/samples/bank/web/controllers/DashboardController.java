package com.seovic.samples.bank.web.controllers;


import com.seovic.samples.bank.services.BankManager;
import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.Account;
import com.seovic.samples.bank.domain.Money;
import com.seovic.samples.bank.domain.Transaction;
import com.seovic.samples.bank.domain.CustomerRepository;
import com.seovic.samples.bank.domain.RepositoryRegistry;
import com.seovic.samples.bank.domain.InsufficientFundsException;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;
import java.util.Currency;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
@Controller
public class DashboardController
    {
    // ---- constructors ----------------------------------------------------

    @Autowired
    public DashboardController(BankManager bankManager)
        {
        m_bankManager = bankManager;
        }


    // ---- data binder initialization --------------------------------------

    @InitBinder
    public void initBinder(WebDataBinder binder)
        {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        }

    
    // ---- handlers --------------------------------------------------------

    @RequestMapping(value = "/dashboard", method = GET)
    public String showDashboard()
        {
        return "dashboard";
        }

    @RequestMapping(value = "/accounts", method = GET)
    public Collection<Account> getAccounts(HttpSession session)
        {
        return getActiveCustomer(session).getAccounts();
        }

    @RequestMapping(value = "/transactions/{accountId}", method = GET)
    public ModelAndView getTransactions(@PathVariable long accountId,
                                        @RequestParam Date from,
                                        @RequestParam Date to,
                                        HttpSession session)
        {
        Account account = getActiveCustomer(session).getAccount(accountId);
        Collection<Transaction> transactions = from != null && to != null
                                                ? account.getTransactions(from, to)
                                                : account.getRecentTransactions();

        return new ModelAndView("transactions", "transactions", transactions);
        }

    @RequestMapping(value = "/account", method = POST)
    public Account createAccount(@RequestParam String description,
                                 @RequestParam String currency,
                                 @RequestParam BigDecimal initialDeposit,
                                 HttpSession session)
        {
        Customer customer        = getActiveCustomer(session);
        Currency accountCurrency = Currency.getInstance(currency);
        Account  account         = m_bankManager.openAccount(
                                        customer, description, accountCurrency);

        m_bankManager.postDeposit(customer, account.getId(),
                "Initial deposit", new Money(initialDeposit, accountCurrency));

        session.setAttribute("customer",
                             getCustomerRepository().getCustomer(customer.getId()));
        
        return account;
        }

    @RequestMapping(value = "/account/{accountId}/billpayment", method = POST)
    public ModelAndView payBill(@PathVariable long accountId,
                                @RequestParam String payee,
                                @RequestParam String description,
                                @RequestParam BigDecimal amount,
                                @RequestParam String currency,
                                HttpSession session)
        {
        try
            {
            m_bankManager.postBillPayment(getActiveCustomer(session), accountId, payee, description, new Money(amount, currency));
            return new ModelAndView("account", "account", getActiveCustomer(session).getAccount(accountId));
            }
        catch (InsufficientFundsException e)
            {
            return new ModelAndView("account", "error", e.getMessage());
            }
        }

    @RequestMapping(value = "/profile", method = GET)
    public Customer getProfile(HttpSession session)
        {
        return getActiveCustomer(session);
        }

    @RequestMapping(value = "/profile", method = POST)
    public ModelAndView updateProfile(Customer c,
                                  HttpSession session)
        {
        Customer customer = getActiveCustomer(session);
        customer.setName(c.getName());
        customer.setEmail(c.getEmail());
        customer.setAddress(c.getAddress());

        m_bankManager.updateProfile(customer);
        session.setAttribute("customer",
                             getCustomerRepository().getCustomer(customer.getId()));

        return new ModelAndView("profile", "customer", customer);
        }

    // ---- helper methods --------------------------------------------------

    private Customer getActiveCustomer(HttpSession session)
        {
        return (Customer) session.getAttribute("customer");
        }


    // ---- dependencies ----------------------------------------------------

    /**
     * Customer repository.
     */
    private transient CustomerRepository m_customerRepository;
    protected CustomerRepository getCustomerRepository()
        {
        if (m_customerRepository == null)
            {
            m_customerRepository = RepositoryRegistry.getCustomerRepository();
            }
        return m_customerRepository;
        }

    public void setCustomerRepository(CustomerRepository customerRepository)
        {
        m_customerRepository = customerRepository;
        }


    // ---- data members ----------------------------------------------------

    private final BankManager m_bankManager;
    }