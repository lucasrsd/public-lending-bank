package com.lucas.bank.transactions.application.service;

import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.transactions.application.port.in.LoadTransactionQuery;
import com.lucas.bank.transactions.application.port.out.LoadTransactionPort;
import com.lucas.bank.transactions.application.port.out.TransactionAggregate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class LoadTransactionService implements LoadTransactionQuery {

    private final LoadTransactionPort loadTransactionPort;
    private final LoadLoanQuery loadLoanQuery;

    @Override
    public TransactionAggregate forLoan(Long loanId) {
        var loan = loadLoanQuery.loadLoan(loanId);
        var transactions = loadTransactionPort.forLoan(loanId);
        return TransactionAggregate
                .builder()
                .transactions(transactions)
                .build();
    }
}
