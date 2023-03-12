package com.lucas.bank.account.adapter.out;

import com.lucas.bank.account.application.port.out.CreateAccountPort;
import com.lucas.bank.account.application.port.out.LoadAccountPort;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.AtomicCounter;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
class AccountPersistenceAdapter implements LoadAccountPort, CreateAccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AtomicCounter atomicCounter;

    @Override
    public Account loadAccount(Long accountId) {
        var account = accountRepository.get(AccountPOJO.of(accountId));
        if (account == null) throw new RuntimeException("Account not found: " + accountId);

        return accountMapper.mapToDomainEntity(account);
    }

    @Override
    public Account createAccount(Account account, UnitOfWork unitOfWork) {

        if (account.getAccountId() == null){
            account.setAccountId(atomicCounter.generate());
        }

        AccountPOJO accountPojo = accountMapper.mapToPOJO(account);

        unitOfWork.addTransaction(accountPojo);
        return accountMapper.mapToDomainEntity(accountPojo);
    }
}
