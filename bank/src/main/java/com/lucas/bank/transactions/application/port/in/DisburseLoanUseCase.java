package com.lucas.bank.transactions.application.port.in;

import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

public interface DisburseLoanUseCase {

    void disburse(Long loanId, PersistenceTransactionManager persistenceTransactionManager);
}
