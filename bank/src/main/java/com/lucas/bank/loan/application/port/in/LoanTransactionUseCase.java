package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanTransactionUseCase {

    void activateLoan(Long loanId, UnitOfWork unitOfWork);
    void makeRepayment(Long loanId, List<Installment> newInstallments, UnitOfWork unitOfWork);
    void dailyAccrual(Long loanId, LocalDateTime bookingDate, UnitOfWork unitOfWork);
}
