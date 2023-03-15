package com.lucas.bank.loan.domain;


import com.lucas.bank.interest.domain.Interest;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Loan {
    private Long loanId;
    private Long accountId;
    private AmortizationType type;
    private BigDecimal amount;
    private LoanState state;
    private Integer term;
    private Interest interest;
    private LocalDateTime creationDate;
    private LocalDateTime disbursementDate;
    private LocalDateTime lastAccrualDate;
    private BigDecimal accruedInterest;
    private List<String> additionalInformation;
    private Integer batchBlock;

    public Boolean canDisburse() {
        return state.equals(LoanState.PENDING_DISBURSEMENT) || state.equals(LoanState.DRAFT);
    }

    public Boolean canRepay(){
        return state.equals(LoanState.ACTIVE) || state.equals(LoanState.LATE);
    }

    public Boolean canAccrue(){
        return state.equals(LoanState.ACTIVE) || state.equals(LoanState.LATE);
    }
}
