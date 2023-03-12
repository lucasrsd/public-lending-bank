package com.lucas.bank.account.application.port.in;

import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface CreateAccountUseCase {

    Account createAccount(CreateAccountCommand command, UnitOfWork unitOfWork);

}
