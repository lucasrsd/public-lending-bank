package com.lucas.bank.account.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbWrapper;
import org.springframework.stereotype.Component;

@Component
public class AccountRepository extends DynamoDbWrapper<AccountPOJO> {
    public AccountRepository() {
        super(AccountPOJO.class, "bank");
    }
}
