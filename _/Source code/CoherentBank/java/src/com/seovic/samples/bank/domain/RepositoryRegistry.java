package com.seovic.samples.bank.domain;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;


/**
 * @author Aleksandar Seovic  2009.11.29
 */
public class RepositoryRegistry
    {
    // ---- repository accessors --------------------------------------------

    public static CustomerRepository getCustomerRepository()
        {
        return getContext().getBean(CustomerRepository.class);
        }

    public static AccountRepository getAccountRepository()
        {
        return getContext().getBean(AccountRepository.class);
        }

    public static TransactionRepository getTransactionRepository()
        {
        return getContext().getBean(TransactionRepository.class);
        }

    public static ExchangeRateRepository getExchangeRateRepository()
        {
        return getContext().getBean(ExchangeRateRepository.class);
        }


    // ---- helper methods --------------------------------------------------

    private static ApplicationContext getContext()
        {
        try
            {
            return ctx.get();
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- static members --------------------------------------------------

    /**
     * Repository context.
     */
    private static final Future<ApplicationContext> ctx;

    /**
     * Initialize context on a background thread to avoid issues with
     * repository instantiation on a cache service thread.
     */
    static
        {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        ctx = exec.submit(new Callable<ApplicationContext>()
            {
            public ApplicationContext call() throws Exception
                {
                return new ClassPathXmlApplicationContext("repository-config.xml");
                }
            });
        }
    }
