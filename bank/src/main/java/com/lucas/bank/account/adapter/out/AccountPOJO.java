package com.lucas.bank.account.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import static com.lucas.bank.shared.staticInformation.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountPOJO {

    private static final String pkPrefix = "account#";
    private static final String skPrefix = "customer-account";

    public static Boolean ofType(String hash, String sort)
    {
        return hash.startsWith(pkPrefix) && sort.equals(skPrefix);
    }

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;

    private Long accountId;
    private String holderName;
    private Long holderBirthDate;
    private Long createdAt;

    public AccountPOJO() {
    }

    public static AccountPOJO of(Long accountId){
        return AccountPOJO.builder().accountId(accountId).build();
    }

    public static String buildPk(Long accountId) {
        return pkPrefix + accountId;
    }

    public static String buildSk() {
        return skPrefix;
    }


    public String getPk() {
        return buildPk(accountId);
    }

    public String getSk() {
        return buildSk();
    }
}