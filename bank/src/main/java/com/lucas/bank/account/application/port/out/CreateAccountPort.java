package com.lucas.bank.account.application.port.out;

import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface CreateAccountPort {
    Account createAccount(Account account, UnitOfWork unitOfWork);
}
