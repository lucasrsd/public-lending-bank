package com.lucas.bank.loan.adapter.out;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoanPOJO {

    private static final String pkPrefix = "loan#";
    private static final String skPrefix = "contract";

    private String pk;
    private String sk;

    private Long loanId;
    private String type;
    private Long accountId;
    private BigDecimal amount;
    private String loanState;
    private Integer term;
    private BigDecimal interestRate;
    private String interestFrequency;
    private Long creationDate;
    private Long disbursementDate;
    private List<String> additionalInformation;
    private Long lastAccrualDate;
    private Integer batchBlock;

    public LoanPOJO() {
    }

    public static String buildPk(Long loanId) {
        return pkPrefix + loanId;
    }

    public static String buildSk() {
        return skPrefix;
    }

    @DynamoDbPartitionKey
    public String getPk() {
        return buildPk(loanId);
    }

    @DynamoDbSortKey
    public String getSk() {
        return buildSk();
    }
}
