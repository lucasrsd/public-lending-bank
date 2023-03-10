package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

@Component
public class InstallmentRepository extends DynamoDbQueryWrapper<InstallmentDataPOJO> {
    public InstallmentRepository() {
        super(InstallmentDataPOJO.class);
    }
}
