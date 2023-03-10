package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.math.BigDecimal;

public interface CreateLoanLedgerEntryUseCase {
    LedgerAggregate disbursement(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate interestApplied(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate taxApplied(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate principalRepayment(Long loanId, BigDecimal affectedPrincipal,PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate interestRepayment(Long loanId, BigDecimal affectedInterest,PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate taxRepayment(Long loanId, BigDecimal affectedTax,PersistenceTransactionManager persistenceTransactionManager);
}
