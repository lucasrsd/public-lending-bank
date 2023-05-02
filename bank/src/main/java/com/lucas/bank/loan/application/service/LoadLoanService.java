package com.lucas.bank.loan.application.service;

import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.installment.application.port.in.LoadInstallmentsQuery;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@UseCase
public class LoadLoanService implements LoadLoanQuery {
    private final LoadLoanPort loadLoanPort;
    private final LoadInstallmentsQuery loadInstallmentsQuery;

    @Override
    public LoanAggregate loadLoan(Long loanId) {
        var loan = loadLoanPort.loadLoan(loanId);
        var installments = loadInstallmentsQuery.loadInstallments(loanId);

        loan.setAdditionalInformation(loan.getInterest().details());

        return LoanAggregate
                .builder()
                .loan(loan)
                .installments(installments)
                .build();
    }

    @Override
    public Map<Long, String> listLoanByBatchBlock(Integer batchBlock, String status) {
        return loadLoanPort.listLoanByBatchBlock(batchBlock, status);
    }

}
