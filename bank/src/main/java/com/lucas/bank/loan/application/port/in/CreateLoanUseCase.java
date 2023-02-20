package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.domain.Loan;

public interface CreateLoanUseCase {

    Long createLoan(CreateLoanCommand command);

}
