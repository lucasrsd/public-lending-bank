package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.shared.dynamoDb.DynamoDbQueryWrapper;
import org.springframework.stereotype.Component;

import static com.lucas.bank.shared.StaticInformation.SINGLE_TABLE_NAME;

@Component
public class InstallmentRepository extends DynamoDbQueryWrapper<InstallmentDataPOJO> {
    public InstallmentRepository() {
        super(InstallmentDataPOJO.class);
    }
}
