package com.lucas.bank.projector.adapter.out.installment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentReadModel {
    private Long loanId;
    private Integer number;
    private String amortizationType;
    private String installmentState;
    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal installmentAmount;
    private BigDecimal taxAmount;
    private BigDecimal remainingBalance;

    private BigDecimal paidPrincipalAmount;
    private BigDecimal paidInterestAmount;
    private BigDecimal paidTaxAmount;

    private BigDecimal taxAdditionalIof;
    private BigDecimal taxDailyIof;
}
