package com.lucas.bank.transaction.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepository extends DynamoDbQueryWrapper<TransactionPOJO> {
    public TransactionRepository() {
        super(TransactionPOJO.class);
    }
}
