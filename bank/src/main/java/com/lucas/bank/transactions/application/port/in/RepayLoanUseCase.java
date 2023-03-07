package com.lucas.bank.transactions.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.math.BigDecimal;
import java.util.List;

public interface RepayLoanUseCase {

    List<Installment> repayment(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager);

}
