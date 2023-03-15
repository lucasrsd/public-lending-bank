package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.shared.SelfValidating;
import com.lucas.bank.tax.application.port.out.TaxAggregate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class CreateInstallmentCommand extends SelfValidating<CreateInstallmentCommand> {

    private final Long loanId;

    @NotNull
    private final BigDecimal amount;

    @NotNull
    private final BigDecimal rate;

    @NotNull
    private final Integer term;

    @NotNull
    private final String amortizationType;

    private final TaxAggregate taxes;

    @NotNull
    private final LocalDateTime disbursementDate;

    public CreateInstallmentCommand(
            Long loanId,
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            String amortizationType,
            TaxAggregate taxes,
            LocalDateTime disbursementDate) {
        this.loanId = loanId;
        this.amount = amount;
        this.rate = rate;
        this.term = term;
        this.amortizationType = amortizationType;
        this.taxes = taxes;
        this.disbursementDate = disbursementDate;
        this.validateSelf();
    }
}
