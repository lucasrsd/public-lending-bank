package com.lucas.bank.transaction.application.service;

import com.lucas.bank.installment.application.port.in.InstallmentRepaymentUseCase;
import com.lucas.bank.installment.application.port.out.InstallmentRepaymentAggregate;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.transaction.application.port.in.RepayLoanUseCase;
import com.lucas.bank.transaction.application.port.out.CreateTransactionPort;
import com.lucas.bank.transaction.domain.RepaymentException;
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
    public InstallmentRepaymentAggregate repayment(Long loanId, BigDecimal amount, UnitOfWork unitOfWork) {

        var loan = loadLoanQuery.loadLoan(loanId);

        if (!loan.getLoan().canRepay()) {
            throw new RepaymentException("Invalid loan state to execute repayment");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RepaymentException("Repayment amount should be greater than 0");
        }

        var newInstallments = installmentRepaymentUseCase.calculateRepayment(loan.getInstallments().getInstallments(), amount);

        loanTransactionUseCase.makeRepayment(loanId, newInstallments.getInstallments(), unitOfWork);
        var transaction = createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.REPAYMENT, loanId, amount), unitOfWork);

        if (newInstallments.getAffectedPrincipal().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.principalRepayment(loanId, transaction, newInstallments.getAffectedPrincipal(), unitOfWork);

        if (newInstallments.getAffectedInterest().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.interestRepayment(loanId, transaction, newInstallments.getAffectedInterest(), unitOfWork);

        if (newInstallments.getAffectedTax().compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.taxRepayment(loanId, transaction, newInstallments.getAffectedTax(), unitOfWork);

        return newInstallments;
    }
}
