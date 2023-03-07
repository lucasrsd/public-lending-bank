package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

import java.util.List;

public interface CreateInstallmentUseCase {

    void create(CreateInstallmentCommand command, PersistenceTransactionManager persistenceTransactionManager);
    List<Installment> preview(CreateInstallmentCommand command);

}
