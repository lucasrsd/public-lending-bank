package com.lucas.bank.transaction.application.port.in;

import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface DisburseLoanUseCase {

    void disburse(Long loanId, UnitOfWork unitOfWork);
}
