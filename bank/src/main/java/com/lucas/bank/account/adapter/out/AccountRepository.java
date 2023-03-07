package com.lucas.bank.account.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

import static com.lucas.bank.shared.StaticInformation.SINGLE_TABLE_NAME;

@Component
public class AccountRepository extends DynamoDbQueryWrapper<AccountPOJO> {
    public AccountRepository() {
        super(AccountPOJO.class);
    }
}
