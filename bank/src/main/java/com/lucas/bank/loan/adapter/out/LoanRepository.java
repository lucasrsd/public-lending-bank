package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbWrapper;
import org.springframework.stereotype.Component;

@Component
public class LoanRepository extends DynamoDbWrapper<LoanPOJO> {
    public LoanRepository() {
        super(LoanPOJO.class, "bank");
    }
}
