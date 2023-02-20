package com.lucas.bank.account.application.port.in;

import com.lucas.bank.account.domain.Account;


public interface LoadAccountQuery {

    Account loadAccount(Long accountId);

}
