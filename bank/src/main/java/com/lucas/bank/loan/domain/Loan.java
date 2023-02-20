package com.lucas.bank.loan.domain;


import com.lucas.bank.interest.domain.Interest;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Loan {
    private final Long loanId;
    private final Long accountId;
    private final AmortizationType type;
    private final BigDecimal amount;
    private final LoanState state;
    private final Integer term;
    private final Interest interest;
    private final Date creationDate;
    private final Date disbursementDate;
    private final Date lastAccrualDate;
    private final Integer batchBlock;
    private List<String> additionalInformation;
}
