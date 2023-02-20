package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbWrapper;
import org.springframework.stereotype.Component;

@Component
public class InstallmentRepository extends DynamoDbWrapper<InstallmentPOJO> {
    public InstallmentRepository() {
        super(InstallmentPOJO.class, "bank");
    }
}
