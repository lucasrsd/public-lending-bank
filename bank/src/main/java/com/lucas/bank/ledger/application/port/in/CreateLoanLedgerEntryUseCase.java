package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

import java.math.BigDecimal;

public interface CreateLoanLedgerEntryUseCase {
    LedgerAggregate disbursement(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork);
    LedgerAggregate interestApplied(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork);
    LedgerAggregate taxApplied(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork);
    LedgerAggregate principalRepayment(Long loanId, Long transactionId, BigDecimal affectedPrincipal, UnitOfWork unitOfWork);
    LedgerAggregate interestRepayment(Long loanId, Long transactionId, BigDecimal affectedInterest, UnitOfWork unitOfWork);
    LedgerAggregate taxRepayment(Long loanId, Long transactionId, BigDecimal affectedTax, UnitOfWork unitOfWork);
}
