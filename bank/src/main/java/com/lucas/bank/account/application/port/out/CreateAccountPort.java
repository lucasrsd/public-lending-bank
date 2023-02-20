package com.lucas.bank.account.application.port.out;

import com.lucas.bank.account.domain.Account;

public interface CreateAccountPort {
    Long createAccount(Account account);
}
