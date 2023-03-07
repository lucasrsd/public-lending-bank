package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.application.port.out.LoanListAggregate;

import java.util.List;


public interface LoadLoanQuery {

    LoanAggregate loadLoan(Long loanId);
    LoanListAggregate listLoans();

}
