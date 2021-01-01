package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.TransactionRepository;


/**
 * @author Aleksandar Seovic  2009.12.04
 */
public class FakeTransactionRepositoryTests
        extends AbstractTransactionRepositoryTests
    {
    protected TransactionRepository createTransactionRepository()
        {
        return new FakeTransactionRepository();
        }
    }