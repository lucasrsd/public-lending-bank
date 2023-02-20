package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.installment.application.port.out.InstallmentAggregate;
import com.lucas.bank.loan.domain.Loan;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class LoanAggregate {
    private final Loan loan;
    private final InstallmentAggregate installments;
}
