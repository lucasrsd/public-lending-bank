package com.lucas.bank.account.application.service;


import com.lucas.bank.account.application.port.in.LoadAccountQuery;
import com.lucas.bank.account.application.port.out.LoadAccountPort;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
class LoadAccountService implements LoadAccountQuery {

    private final LoadAccountPort loadAccountPort;

    @Override
    public Account loadAccount(Long accountId) {
        return loadAccountPort.loadAccount(accountId);
    }
}
