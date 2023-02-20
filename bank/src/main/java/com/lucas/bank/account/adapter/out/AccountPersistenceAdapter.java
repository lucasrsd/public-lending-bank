package com.lucas.bank.account.adapter.out;

import com.lucas.bank.account.application.port.out.CreateAccountPort;
import com.lucas.bank.account.application.port.out.LoadAccountPort;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class AccountPersistenceAdapter implements LoadAccountPort, CreateAccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(Long accountId) {
        var account = accountRepository.get(AccountPOJO.buildPk(accountId), AccountPOJO.buildSk());
        if (account == null) throw new RuntimeException("Account not found: " + accountId);

        return accountMapper.mapToDomainEntity(account);
    }

    @Override
    public Long createAccount(Account account) {
        AccountPOJO entity = accountMapper.mapToPOJO(account);
        accountRepository.put(entity);
        return entity.getAccountId();
    }
}
