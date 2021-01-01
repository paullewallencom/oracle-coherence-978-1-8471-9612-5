package com.seovic.samples.bank.web.controllers;


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.seovic.samples.bank.domain.Customer;
import com.seovic.samples.bank.domain.CustomerRepository;
import com.seovic.samples.bank.domain.RepositoryRegistry;
import com.seovic.samples.bank.domain.Address;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


/**
 */
@Controller
@RequestMapping("/register")
public class RegistrationController
    {
    // ---- handlers --------------------------------------------------------

    @RequestMapping(method = GET)
    public String get()
        {
        return "register";
        }

    @RequestMapping(method = POST)
    public String register(Customer c,
                           HttpSession session)
        {
        Customer customer = Customer.create(
                c.getName(), c.getUsername(), c.getPassword(), c.getEmail());
        customer.setAddress(c.getAddress());
        
        getCustomerRepository().save(customer);

        session.setAttribute("customer", customer);
        return "redirect:dashboard";
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
}
