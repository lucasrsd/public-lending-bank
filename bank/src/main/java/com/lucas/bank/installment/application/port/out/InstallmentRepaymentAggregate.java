package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.Installment;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class InstallmentRepaymentAggregate {
    private final BigDecimal affectedPrincipal;
    private final BigDecimal affectedInterest;
    private final BigDecimal affectedTax;
    private final List<Installment> installments;
}
