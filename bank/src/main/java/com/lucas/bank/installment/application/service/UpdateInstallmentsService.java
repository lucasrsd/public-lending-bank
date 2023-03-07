package com.lucas.bank.installment.application.service;

import com.lucas.bank.installment.application.port.in.UpdateInstallmentUseCase;
import com.lucas.bank.installment.application.port.out.UpdateInstallmentPort;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class UpdateInstallmentsService implements UpdateInstallmentUseCase {

    private final UpdateInstallmentPort updateInstallmentPort;
    private final LoadLoanQuery loadLoanQuery;

    @Override
    public void updateInstallments(Long loanId, List<Installment> installments, PersistenceTransactionManager persistenceTransactionManager) {
        var loan = loadLoanQuery.loadLoan(loanId);
        updateInstallmentPort.updateInstallments(loanId, loan.getLoan().getType(), installments, persistenceTransactionManager);
    }
}
