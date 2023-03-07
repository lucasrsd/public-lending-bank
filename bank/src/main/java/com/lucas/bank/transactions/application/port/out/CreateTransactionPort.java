package com.lucas.bank.transactions.application.port.out;

import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.transactions.domain.Transaction;

public interface CreateTransactionPort {
    Long createTransaction(Transaction transaction, PersistenceTransactionManager persistenceTransactionManager);
}
