package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.util.List;

public interface CreateInstallmentPort {
    void createInstallment(AmortizationType amortizationType, Long loanId, List<Installment> installments, PersistenceTransactionManager persistenceTransactionManager);
}
