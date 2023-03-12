package com.lucas.bank.transaction.application.service;

import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.transaction.application.port.in.InterestAppliedLoanUseCase;
import com.lucas.bank.transaction.application.port.out.CreateTransactionPort;
import com.lucas.bank.transaction.domain.Transaction;
import com.lucas.bank.transaction.domain.TransactionType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
@UseCase
public class InterestAppliedLoanService implements InterestAppliedLoanUseCase {

    private final CreateTransactionPort createTransactionPort;
    private final CreateLoanLedgerEntryUseCase createLoanLedgerEntryUseCase;


    @Override
    public void applyInterest(Long loanId, BigDecimal interestAmount, UnitOfWork unitOfWork) {
        var transaction = createTransactionPort.createTransaction(Transaction.withoutId(TransactionType.INTEREST_APPLIED, loanId, interestAmount), unitOfWork);
        createLoanLedgerEntryUseCase.interestApplied(loanId, transaction, interestAmount, unitOfWork);
    }
}
