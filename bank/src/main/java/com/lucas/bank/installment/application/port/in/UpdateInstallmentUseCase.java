package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.util.List;

public interface UpdateInstallmentUseCase {

    void updateInstallments(Long loanId, List<Installment> installments, PersistenceTransactionManager persistenceTransactionManager);
}
