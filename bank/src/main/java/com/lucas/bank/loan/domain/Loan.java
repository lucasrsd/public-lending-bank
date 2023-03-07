package com.lucas.bank.loan.domain;


import com.lucas.bank.interest.domain.Interest;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Loan {
    private final Long loanId;
    private final Long accountId;
    private final AmortizationType type;
    private BigDecimal amount;
    private LoanState state;
    private Integer term;
    private Interest interest;
    private Date creationDate;
    private Date disbursementDate;
    private Date lastAccrualDate;
    private List<String> additionalInformation;

    public Boolean canDisburse() {
        return state.equals(LoanState.PENDING_DISBURSEMENT) || state.equals(LoanState.DRAFT);
    }

    public Boolean canRepay(){
        return state.equals(LoanState.ACTIVE) || state.equals(LoanState.LATE);
    }
}
