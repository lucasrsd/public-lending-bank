package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface UpdateLoanPort {
    Loan updateLoan(Loan loan, UnitOfWork unitOfWork);
}
