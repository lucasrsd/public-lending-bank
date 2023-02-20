package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;

public interface CreateLoanPort {
    Long createLoan(Loan loan);
}
