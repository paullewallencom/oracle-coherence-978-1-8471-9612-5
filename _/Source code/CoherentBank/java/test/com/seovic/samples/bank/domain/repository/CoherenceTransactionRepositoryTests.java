package com.seovic.samples.bank.domain.repository;


import com.seovic.samples.bank.domain.TransactionRepository;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * @author Aleksandar Seovic  2009.12.04
 */
public class CoherenceTransactionRepositoryTests
        extends AbstractTransactionRepositoryTests
    {
    protected TransactionRepository createTransactionRepository()
        {
        return new CoherenceTransactionRepository();
        }
    }
