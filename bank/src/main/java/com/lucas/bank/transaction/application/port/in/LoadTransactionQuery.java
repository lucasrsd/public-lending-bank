package com.lucas.bank.transaction.application.port.in;

import com.lucas.bank.transaction.application.port.out.TransactionAggregate;


public interface LoadTransactionQuery {

    TransactionAggregate forLoan(Long loanId);
}
