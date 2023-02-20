package com.lucas.bank.account.application.port.in;


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
public class CreateAccountCommand extends SelfValidating<CreateAccountCommand> {
    @NotNull
    private final String holderName;

    @NotNull
    private final Date holderBirthDate;

    public CreateAccountCommand(String holderName, Date holderBirthDate) {
        this.holderName = holderName;
        this.holderBirthDate = holderBirthDate;
        this.validateSelf();
    }
}
