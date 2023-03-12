package com.lucas.bank.transaction.application.port.out;

import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.transaction.domain.Transaction;

public interface CreateTransactionPort {
    Long createTransaction(Transaction transaction, UnitOfWork unitOfWork);
}
