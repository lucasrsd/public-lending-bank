package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface LoanTransactionUseCase {

    void activateLoan(Long loanId, PersistenceTransactionManager persistenceTransactionManager);
    void makeRepayment(Long loanId, List<Installment> newInstallments, PersistenceTransactionManager persistenceTransactionManager);
    void dailyAccrual(Long loanId, Date date, PersistenceTransactionManager persistenceTransactionManager);
}
