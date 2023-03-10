package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

@Component
public class LoanRepository extends DynamoDbQueryWrapper<LoanPOJO> {
    public LoanRepository() {
        super(LoanPOJO.class);
    }
}
