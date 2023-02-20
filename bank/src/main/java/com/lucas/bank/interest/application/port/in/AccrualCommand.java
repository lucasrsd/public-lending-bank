package com.lucas.bank.interest.application.port.in;

import com.lucas.bank.shared.SelfValidating;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class AccrualCommand extends SelfValidating<AccrualCommand> {
    @NotNull
    private final Long loanAccountId;

    @NotNull
    private final Date referenceDate;

    public AccrualCommand(Long loanAccountId, Date referenceDate) {
        this.loanAccountId = loanAccountId;
        this.referenceDate = referenceDate;
        this.validateSelf();
    }
}

