package com.lucas.bank.transaction.application.port.in;

import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

public interface DisburseLoanUseCase {

    void disburse(Long loanId, PersistenceTransactionManager persistenceTransactionManager);
}
