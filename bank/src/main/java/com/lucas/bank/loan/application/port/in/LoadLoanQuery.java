package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.loan.application.port.out.LoanAggregate;

import java.util.Map;


public interface LoadLoanQuery {

    LoanAggregate loadLoan(Long loanId);
    Map<Long, String> listLoanByBatchBlock(Integer batchBlock, String status);

}
