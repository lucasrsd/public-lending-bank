package com.lucas.bank.transactions.application.port.in;

import com.lucas.bank.transactions.application.port.out.TransactionAggregate;


public interface LoadTransactionQuery {

    TransactionAggregate forLoan(Long loanId);
}
