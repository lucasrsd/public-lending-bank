package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.math.BigDecimal;
import java.util.Date;

public interface CreateLedgerEntryUseCase {
    LedgerAggregate forLoanDisbursement(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate forPrincipalRepayment(Long loanId, BigDecimal affectedPrincipal,PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate forInterestRepayment(Long loanId, BigDecimal affectedInterest,PersistenceTransactionManager persistenceTransactionManager);
    LedgerAggregate forTaxRepayment(Long loanId, BigDecimal affectedTax,PersistenceTransactionManager persistenceTransactionManager);
}
