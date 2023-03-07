package com.lucas.bank.transactions.application.port.out;

import com.lucas.bank.transactions.domain.Transaction;

import java.util.List;

public interface LoadTransactionPort {
    List<Transaction> forLoan(Long loanId);
}
