package com.lucas.bank.transaction.application.port.out;

import com.lucas.bank.transaction.domain.Transaction;

import java.util.List;

public interface LoadTransactionPort {
    List<Transaction> forLoan(Long loanId);
}
