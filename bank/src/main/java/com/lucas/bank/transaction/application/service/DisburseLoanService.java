package com.lucas.bank.transaction.application.service;

import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
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
    public void disburse(Long loanId, PersistenceTransactionManager persistenceTransactionManager) {

        var loanAggregate = loadLoanQuery.loadLoan(loanId);

        loanTransactionUseCase.activateLoan(loanId, persistenceTransactionManager);

        BigDecimal disbursementAmount = loanAggregate.getLoan().getAmount();
        BigDecimal totalTaxAmount = loanAggregate.getInstallments().getDetails().getTaxTotalAmount();

        createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.DISBURSEMENT, loanId, disbursementAmount), persistenceTransactionManager);

        if (disbursementAmount.compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.disbursement(loanId, disbursementAmount, persistenceTransactionManager);

        if (totalTaxAmount.compareTo(BigDecimal.ZERO) > 0)
            createLoanLedgerEntryUseCase.taxApplied(loanId, totalTaxAmount, persistenceTransactionManager);
    }
}
