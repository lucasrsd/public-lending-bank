package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.application.port.out.LoanListAggregate;

import java.util.List;
import java.util.Map;


public interface LoadLoanQuery {

    LoanAggregate loadLoan(Long loanId);
    LoanListAggregate listLoans();
    Map<Long, String> listLoanByBatchBlock(Integer batchBlock, String status);

}
