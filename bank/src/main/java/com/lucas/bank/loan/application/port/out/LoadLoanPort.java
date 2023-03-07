package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;

import java.util.List;

public interface LoadLoanPort {
    Loan loadLoan(Long loanId);
    List<Loan> listLoans();
}
