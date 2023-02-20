package com.lucas.bank.account.adapter.out;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountPOJO {

    private static final String pkPrefix = "account#";
    private static final String skPrefix = "customer-account";
    private String pk;
    private String sk;
    private Long accountId;
    private String holderName;
    private Long holderBirthDate;
    private Long createdAt;
    private Boolean active;

    public AccountPOJO() {
    }

    public static String buildPk(Long accountId) {
        return pkPrefix + accountId;
    }

    public static String buildSk() {
        return skPrefix;
    }

    @DynamoDbPartitionKey
    public String getPk() {
        return buildPk(accountId);
    }

    @DynamoDbSortKey
    public String getSk() {
        return buildSk();
    }
}