package com.lucas.bank.account.application.port.out;

import com.lucas.bank.account.domain.Account;

public interface LoadAccountPort {

    Account loadAccount(Long accountId);
}
