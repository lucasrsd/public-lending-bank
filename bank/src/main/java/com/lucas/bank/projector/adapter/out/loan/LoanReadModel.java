package com.lucas.bank.projector.adapter.out.loan;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanReadModel {

    private Long loanId;
    private String type;
    private Long accountId;
    private BigDecimal amount;
    private String loanState;
    private Integer term;
    private BigDecimal interestRate;
    private String interestFrequency;
    private LocalDateTime creationDate;
    private LocalDateTime disbursementDate;
    private LocalDateTime lastAccrualDate;
    private BigDecimal accruedInterest;
    private Integer batchBlock;
}
