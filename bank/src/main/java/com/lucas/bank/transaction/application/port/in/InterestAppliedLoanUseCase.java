package com.lucas.bank.transaction.application.port.in;

import com.lucas.bank.shared.persistenceManager.UnitOfWork;

import java.math.BigDecimal;

public interface InterestAppliedLoanUseCase {

    void applyInterest(Long loanId, BigDecimal interestAmount, UnitOfWork unitOfWork);
}
