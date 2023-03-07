package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.util.List;

public interface UpdateLoanUseCase {

    LoanAggregate activateLoan(Long loanId, PersistenceTransactionManager persistenceTransactionManager);
    LoanAggregate makeRepayment(Long loanId, List<Installment> newInstallments, PersistenceTransactionManager persistenceTransactionManager);
}
