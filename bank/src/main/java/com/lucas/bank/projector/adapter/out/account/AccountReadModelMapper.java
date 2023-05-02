package com.lucas.bank.projector.adapter.out.account;

import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class AccountReadModelMapper {

    public AccountReadModel mapToDomainEntity(AccountPOJO account) {
        return AccountReadModel
                .builder()
                .accountId(account.getAccountId())
                .holderName(account.getHolderName())
                .holderBirthDate(DateTimeUtil.from(account.getHolderBirthDate()))
                .createdAt(DateTimeUtil.from(account.getCreatedAt()))
                .build();
    }
}
