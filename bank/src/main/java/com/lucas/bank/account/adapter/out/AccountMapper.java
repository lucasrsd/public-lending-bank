package com.lucas.bank.account.adapter.out;

import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;


@Component
class AccountMapper {

    Account mapToDomainEntity(AccountPOJO account) {
        return Account
                .builder()
                .accountId(account.getAccountId())
                .holderName(account.getHolderName())
                .holderBirthDate(DateTimeUtil.from(account.getHolderBirthDate()))
                .active(account.getActive())
                .createdAt(DateTimeUtil.from(account.getCreatedAt()))
                .build();
    }

    AccountPOJO mapToPOJO(Account account) {
        return AccountPOJO
                .builder()
                .accountId(account.getAccountId())
                .holderName(account.getHolderName())
                .holderBirthDate(DateTimeUtil.to(account.getHolderBirthDate()))
                .createdAt(DateTimeUtil.to(account.getCreatedAt()))
                .active(account.getActive())
                .build();
    }

}
