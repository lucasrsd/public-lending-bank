package com.lucas.bank.transaction.application.service;

import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.transaction.application.port.in.DisburseLoanUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.transaction.application.port.out.CreateTransactionPort;
import com.lucas.bank.transaction.domain.Transaction;
import com.lucas.bank.transaction.domain.TransactionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
@UseCase
public class DisburseLoanService implements DisburseLoanUseCase {

    private final LoanTransactionUseCase loanTransactionUseCase;
    private final CreateTransactionPort createTransactionPort;
    private final CreateLoanLedgerEntryUseCase createLoanLedgerEntryUseCase;
    private final LoadLoanQuery loadLoanQuery;

    @Override
    public void disburse(Long loanId, UnitOfWork unitOfWork) {

        var loanAggregate = loadLoanQuery.loadLoan(loanId);

        loanTransactionUseCase.activateLoan(loanId, unitOfWork);

        BigDecimal disbursementAmount = loanAggregate.getLoan().getAmount();
        BigDecimal totalTaxAmount = loanAggregate.getInstallments().getDetails().getTaxTotalAmount();

        var transaction = createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.DISBURSEMENT, loanId, disbursementAmount), unitOfWork);

        // ToDO - check past disbursements with interest applied pending to be executed

        if (disbursementAmount.compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.disbursement(loanId, transaction, disbursementAmount, unitOfWork);

        if (totalTaxAmount.compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.taxApplied(loanId, transaction, totalTaxAmount, unitOfWork);
    }
}
