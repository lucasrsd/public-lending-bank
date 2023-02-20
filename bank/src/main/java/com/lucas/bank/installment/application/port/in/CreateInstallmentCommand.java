package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.account.application.port.in.CreateAccountCommand;
import com.lucas.bank.shared.SelfValidating;
import com.lucas.bank.taxes.application.port.out.TaxAggregate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

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
    private final Date disbursementDate;

    public CreateInstallmentCommand(
            Long loanId,
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            String amortizationType,
            TaxAggregate taxes,
            Date disbursementDate) {
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
