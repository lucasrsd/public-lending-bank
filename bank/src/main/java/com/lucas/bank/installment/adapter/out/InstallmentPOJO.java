package com.lucas.bank.installment.adapter.out;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


import java.math.BigDecimal;
import java.util.Map;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InstallmentPOJO {

    private static final String pkPrefix = "loan-installments#";
    private static final String skPrefix = "installment-number#";

    private String pk;
    private String sk;

    private Long loanId;
    private Integer number;
    private String amortizationType;
    private String installmentState;
    private Long dueDate;
    private Long paymentDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal installmentAmount;
    private BigDecimal taxAmount;
    private BigDecimal remainingBalance;
    private Map<String, BigDecimal> taxComposition;

    public InstallmentPOJO(){

    }

    public static String buildPk(Long loanId) {
        return pkPrefix + loanId;
    }

    public static String buildSk(Integer number) {
        return skPrefix + number;
    }

    @DynamoDbPartitionKey
    public String getPk() {
        return buildPk(loanId);
    }

    @DynamoDbSortKey
    public String getSk() {
        return buildSk(number);
    }
}
