package com.lucas.bank.loan.application.port.in;

import com.lucas.bank.shared.SelfValidating;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class CreateLoanCommand extends SelfValidating<CreateLoanCommand> {

    @NotNull
    private final Long accountId;

    @NotNull
    private final String amortizationType;

    @NotNull
    private final BigDecimal amount;

    @NotNull
    private final Integer term;

    @NotNull
    private final BigDecimal interestRate;

    @NotNull
    private final String interestFrequency;

    private final String tax;

    private final Date disbursementDate;

    public CreateLoanCommand(
            Long accountId,
            String amortizationType,
            BigDecimal amount,
            Integer term,
            BigDecimal interestRate,
            String interestFrequency,
            String tax,
            Date disbursementDate) {
        this.accountId = accountId;
        this.amortizationType = amortizationType;
        this.amount = amount;
        this.term = term;
        this.interestRate = interestRate;
        this.interestFrequency = interestFrequency;
        this.tax = tax;
        this.disbursementDate = disbursementDate;
        this.validateSelf();
    }
}
