package com.lucas.bank.account.adapter.out;

import com.lucas.bank.account.application.port.out.CreateAccountPort;
import com.lucas.bank.account.application.port.out.LoadAccountPort;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class AccountPersistenceAdapter implements LoadAccountPort, CreateAccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(Long accountId) {
        var account = accountRepository.get(AccountPOJO.of(accountId));
        if (account == null) throw new RuntimeException("Account not found: " + accountId);

        return accountMapper.mapToDomainEntity(account);
    }

    @Override
    public Account createAccount(Account account, PersistenceTransactionManager persistenceTransactionManager) {
        AccountPOJO accountPojo = accountMapper.mapToPOJO(account);
        persistenceTransactionManager.addTransaction(accountPojo);
        return accountMapper.mapToDomainEntity(accountPojo);
    }
}
