package com.lucas.bank.account.application.port.in;

import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;

public interface CreateAccountUseCase {

    Account createAccount(CreateAccountCommand command, PersistenceTransactionManager persistenceTransactionManager);

}
