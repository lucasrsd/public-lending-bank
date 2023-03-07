package com.lucas.bank.transactions.application.service;

import com.lucas.bank.installment.application.port.in.InstallmentRepaymentUseCase;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.UpdateLoanUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.transactions.application.port.in.RepayLoanUseCase;
import com.lucas.bank.transactions.application.port.out.CreateTransactionPort;
import com.lucas.bank.transactions.domain.Transaction;
import com.lucas.bank.transactions.domain.TransactionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@UseCase
public class RepayLoanService implements RepayLoanUseCase {
    private final LoadLoanQuery loadLoanQuery;
    private final UpdateLoanUseCase updateLoanUseCase;
    private final InstallmentRepaymentUseCase installmentRepaymentUseCase;
    private final CreateTransactionPort createTransactionPort;
    private final CreateLedgerEntryUseCase createLedgerEntryUseCase;

    @Override
    public List<Installment> repayment(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager) {

        var loan = loadLoanQuery.loadLoan(loanId);

        if (!loan.getLoan().canRepay()) {
            throw new RuntimeException("Invalid loan state to execute repayment");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Repayment amount should be greater than 0");
        }

        var newInstallments = installmentRepaymentUseCase.calculateRepayment(loan.getInstallments().getInstallments(), amount);

        // ToDo - Add affected amount to include in the ledger

        updateLoanUseCase.makeRepayment(loanId, newInstallments.getInstallments(), persistenceTransactionManager);
        createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.REPAYMENT, loanId, amount), persistenceTransactionManager);

        if (newInstallments.getAffectedPrincipal().compareTo(BigDecimal.ZERO) > 0)
            createLedgerEntryUseCase.forPrincipalRepayment(loanId, newInstallments.getAffectedPrincipal(), persistenceTransactionManager);

        if (newInstallments.getAffectedInterest().compareTo(BigDecimal.ZERO) > 0)
            createLedgerEntryUseCase.forInterestRepayment(loanId, newInstallments.getAffectedInterest(), persistenceTransactionManager);

        if (newInstallments.getAffectedTax().compareTo(BigDecimal.ZERO) > 0)
            createLedgerEntryUseCase.forTaxRepayment(loanId, newInstallments.getAffectedTax(), persistenceTransactionManager);

        return newInstallments.getInstallments();
    }
}
