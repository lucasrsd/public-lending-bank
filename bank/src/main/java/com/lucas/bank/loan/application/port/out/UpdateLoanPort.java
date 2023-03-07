package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

public interface UpdateLoanPort {
    Loan updateLoan(Loan loan, PersistenceTransactionManager persistenceTransactionManager);
}
