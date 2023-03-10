package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

@Component
public class LedgerRepository extends DynamoDbQueryWrapper<LedgerPOJO> {
    public LedgerRepository() {
        super(LedgerPOJO.class);
    }
}
