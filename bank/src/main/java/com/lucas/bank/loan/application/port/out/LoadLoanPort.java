package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;

public interface LoadLoanPort {
    Loan loadLoan(Long loanId);
}
