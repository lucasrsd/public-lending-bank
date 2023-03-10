package com.lucas.bank.account.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

@Component
public class AccountRepository extends DynamoDbQueryWrapper<AccountPOJO> {
    public AccountRepository() {
        super(AccountPOJO.class);
    }
}
