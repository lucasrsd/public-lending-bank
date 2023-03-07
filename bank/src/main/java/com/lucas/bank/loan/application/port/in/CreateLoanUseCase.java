package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

public interface CreateLoanUseCase {

    Long createLoan(CreateLoanCommand command, PersistenceTransactionManager persistenceTransactionManager);

}
