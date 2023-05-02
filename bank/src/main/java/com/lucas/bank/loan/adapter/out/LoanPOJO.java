package com.lucas.bank.loan.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static com.lucas.bank.shared.staticInformation.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoanPOJO {

    public static final String pkPrefix = "loan#";
    public static final String skPrefix = "contract";

    public static Boolean ofType(String hash, String sort)
    {
        return hash.startsWith(pkPrefix) && sort.equals(skPrefix);
    }

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;


    private Long loanId;
    private String type;
    private Long accountId;
    private BigDecimal amount;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = StaticInformation.LOAN_STATE_BY_BATCH_BLOCK_GSI_INDEX)
    private String loanState;
    private Integer term;
    private BigDecimal interestRate;
    private String interestFrequency;
    private Long creationDate;
    private Long disbursementDate;
    private List<String> additionalInformation;
    private Long lastAccrualDate;
    private BigDecimal accruedInterest;

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = StaticInformation.LOAN_STATE_BY_BATCH_BLOCK_GSI_INDEX)
    private Integer batchBlock;

    public LoanPOJO() {
    }

    public static LoanPOJO of(Long loanId){
        return LoanPOJO.builder().loanId(loanId).build();
    }

    public static LoanPOJO of(String state, Integer batchBlock){
        return LoanPOJO.builder().loanState(state).batchBlock(batchBlock).build();
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
