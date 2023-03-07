package com.lucas.bank.installment.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;


import java.math.BigDecimal;
import java.util.Map;

@DynamoDBDocument
@Getter
@Setter
@AllArgsConstructor
@Builder
public class InstallmentPOJO {
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

    private BigDecimal paidPrincipalAmount;
    private BigDecimal paidInterestAmount;
    private BigDecimal paidTaxAmount;

    public InstallmentPOJO(){

    }
}
