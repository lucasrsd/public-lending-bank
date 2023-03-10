package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;

import java.util.List;
import java.util.Map;

public interface LoadLoanPort {
    Loan loadLoan(Long loanId);
    List<Loan> listLoans();
    Map<Long, String> listLoanByBatchBlock(Integer batchBlock, String status);
}
