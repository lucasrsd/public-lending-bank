package com.lucas.bank.taxes.application.port.in;

import com.lucas.bank.loan.application.port.in.CreateLoanCommand;
import com.lucas.bank.shared.SelfValidating;
import com.lucas.bank.taxes.domain.InstallmentDetails;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class CalculateTaxesCommand extends SelfValidating<CreateLoanCommand> {

    @NotNull
    private final String taxType;

    @NotNull
    private final Date disbusementDate;
    @NotNull
    private final List<InstallmentDetails> installmentDetails;

    public CalculateTaxesCommand(String taxType,
                                 Date disbusementDate,
                                 List<InstallmentDetails> installmentDetails) {
        this.taxType = taxType;
        this.disbusementDate = disbusementDate;
        this.installmentDetails = installmentDetails;
        this.validateSelf();
    }
}
