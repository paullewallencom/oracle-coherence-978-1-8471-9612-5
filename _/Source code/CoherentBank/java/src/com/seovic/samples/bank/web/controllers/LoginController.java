package com.seovic.samples.bank.web.controllers;


import com.seovic.samples.bank.services.BankManager;
import com.seovic.samples.bank.domain.Customer;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


/**
 * @author Aleksandar Seovic  2009.12.02
 */
@Controller
@RequestMapping("/login")
public class LoginController
    {
    // ---- constructors ----------------------------------------------------

    @Autowired
    public LoginController(BankManager bankManager)
        {
        m_bankManager = bankManager;
        }

    // ---- handlers --------------------------------------------------------

    @RequestMapping(method = GET)
    public String get()
        {
        return "login";
        }

    @RequestMapping(method = POST)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session)
        {
        Customer customer = m_bankManager.authenticate(username, password);
        if (customer == null)
            {
            return "login";
            }
        else
            {
            session.setAttribute("customer", customer);
            return "redirect:dashboard";
            }
        }

    // ---- data members ----------------------------------------------------

    private final BankManager m_bankManager;
    }
