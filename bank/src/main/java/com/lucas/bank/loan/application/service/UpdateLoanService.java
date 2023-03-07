package com.lucas.bank.loan.application.service;

import com.lucas.bank.installment.application.port.in.UpdateInstallmentUseCase;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.installment.domain.InstallmentState;
import com.lucas.bank.loan.application.port.in.UpdateLoanUseCase;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.application.port.out.UpdateLoanPort;
import com.lucas.bank.loan.domain.LoanState;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class UpdateLoanService implements UpdateLoanUseCase {
    private final UpdateLoanPort updateLoanPort;
    private final LoadLoanPort loadLoanPort;
    private final UpdateInstallmentUseCase updateInstallmentUseCase;

    @Override
    public LoanAggregate activateLoan(Long loanId, PersistenceTransactionManager persistenceTransactionManager) {
        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canDisburse()){
            throw new RuntimeException("Invalid loan account state to execute disbursement");
        }

        loan.setState(LoanState.ACTIVE);
        loan.setDisbursementDate(new Date());
        return LoanAggregate.builder().loan(updateLoanPort.updateLoan(loan, persistenceTransactionManager)).build();
    }

    @Override
    public LoanAggregate makeRepayment(Long loanId, List<Installment> newInstallments, PersistenceTransactionManager persistenceTransactionManager) {

        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canRepay()){
            throw new RuntimeException("Invalid loan account state to finalize repayment");
        }

        if (newInstallments.stream().allMatch(i -> i.getState().equals(InstallmentState.PAID))){
            loan.setState(LoanState.PAID);
        }

        updateInstallmentUseCase.updateInstallments(loanId, newInstallments, persistenceTransactionManager);

        return LoanAggregate.builder().loan(updateLoanPort.updateLoan(loan, persistenceTransactionManager)).build();
    }
}
