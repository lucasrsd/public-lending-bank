package com.lucas.bank.transactions.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

import static com.lucas.bank.shared.StaticInformation.SINGLE_TABLE_NAME;

@Component
public class TransactionRepository extends DynamoDbQueryWrapper<TransactionPOJO> {
    public TransactionRepository() {
        super(TransactionPOJO.class);
    }
}
