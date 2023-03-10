package com.lucas.bank.transaction.application.service;

import com.lucas.bank.installment.application.port.in.InstallmentRepaymentUseCase;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.transaction.application.port.in.RepayLoanUseCase;
import com.lucas.bank.transaction.application.port.out.CreateTransactionPort;
import com.lucas.bank.transaction.domain.Transaction;
import com.lucas.bank.transaction.domain.TransactionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@UseCase
public class RepayLoanService implements RepayLoanUseCase {
    private final LoadLoanQuery loadLoanQuery;
    private final LoanTransactionUseCase loanTransactionUseCase;
    private final InstallmentRepaymentUseCase installmentRepaymentUseCase;
    private final CreateTransactionPort createTransactionPort;
    private final CreateLoanLedgerEntryUseCase createLoanLedgerEntryUseCase;

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

        loanTransactionUseCase.makeRepayment(loanId, newInstallments.getInstallments(), persistenceTransactionManager);
        createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.REPAYMENT, loanId, amount), persistenceTransactionManager);

        if (newInstallments.getAffectedPrincipal().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.principalRepayment(loanId, newInstallments.getAffectedPrincipal(), persistenceTransactionManager);

        if (newInstallments.getAffectedInterest().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.interestRepayment(loanId, newInstallments.getAffectedInterest(), persistenceTransactionManager);

        if (newInstallments.getAffectedTax().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.taxRepayment(loanId, newInstallments.getAffectedTax(), persistenceTransactionManager);

        return newInstallments.getInstallments();
    }
}
