package com.lucas.bank.transaction.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.lucas.bank.shared.staticInformation.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionPOJO {

    public static final String pkPrefix = "loan#";
    public static final String skPrefix = "transaction#";

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;

    private Long transactionId;
    private Long loanId;
    private Long date;
    private String type;
    private BigDecimal amount;

    public TransactionPOJO() {
    }

    public static TransactionPOJO of(Long loanId, Long transactionId){
        return TransactionPOJO.builder().loanId(loanId).transactionId(transactionId).build();
    }

    public static TransactionPOJO of(Long loanId){
        return TransactionPOJO.builder().loanId(loanId).build();
    }


    public static String buildPk(Long loanId) {
        return pkPrefix + loanId;
    }

    public static String buildSk(Long transactionId) {
        return skPrefix + transactionId;
    }

    public String getPk() {
        return buildPk(loanId);
    }

    public String getSk() {
        return buildSk(transactionId);
    }
}
