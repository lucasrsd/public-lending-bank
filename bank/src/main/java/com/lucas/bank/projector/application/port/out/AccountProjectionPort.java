package com.lucas.bank.projector.application.port.out;

import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface AccountProjectionPort {
    void project(AccountPOJO account);
}
