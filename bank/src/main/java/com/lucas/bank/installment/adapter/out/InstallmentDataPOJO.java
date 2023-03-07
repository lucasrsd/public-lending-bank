package com.lucas.bank.installment.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.lucas.bank.shared.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InstallmentDataPOJO {

    private static final String pkPrefix = "loan#";
    private static final String skPrefix = "installments";

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;

    private Long loanId;
    private List<InstallmentPOJO> installments;

    public InstallmentDataPOJO() {

    }

    public static InstallmentDataPOJO of(Long loanId){
        return InstallmentDataPOJO.builder().loanId(loanId).build();
    }

    public static String buildPk(Long loanId) {
        return pkPrefix + loanId;
    }

    public static String buildSk() {
        return skPrefix;
    }


    public String getPk() {
        return buildPk(loanId);
    }


    public String getSk() {
        return buildSk();
    }
}
