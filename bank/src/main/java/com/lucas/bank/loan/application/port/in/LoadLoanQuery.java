package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.loan.application.port.out.LoanAggregate;


public interface LoadLoanQuery {

    LoanAggregate loadLoan(Long loanAccountId);

}
