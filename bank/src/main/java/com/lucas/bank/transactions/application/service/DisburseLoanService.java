package com.lucas.bank.transactions.application.service;

import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.UpdateLoanUseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.transactions.application.port.in.DisburseLoanUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.transactions.application.port.out.CreateTransactionPort;
import com.lucas.bank.transactions.domain.Transaction;
import com.lucas.bank.transactions.domain.TransactionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
@UseCase
public class DisburseLoanService implements DisburseLoanUseCase {

    private final UpdateLoanUseCase updateLoanUseCase;
    private final CreateTransactionPort createTransactionPort;
    private final CreateLedgerEntryUseCase createLedgerEntryUseCase;

    @Override
    public void disburse(Long loanId, PersistenceTransactionManager persistenceTransactionManager) {

        var loanAggregate = updateLoanUseCase.activateLoan(loanId, persistenceTransactionManager);

        BigDecimal disbursementAmount = loanAggregate.getLoan().getAmount();

        createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.DISBURSEMENT, loanId, disbursementAmount), persistenceTransactionManager);

        createLedgerEntryUseCase.forLoanDisbursement(loanId, disbursementAmount, persistenceTransactionManager);
    }
}
