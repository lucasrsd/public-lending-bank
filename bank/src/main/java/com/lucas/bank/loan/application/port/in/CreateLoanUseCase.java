package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface CreateLoanUseCase {

    Long createLoan(CreateLoanCommand command, UnitOfWork unitOfWork);

}
