package com.lucas.bank.installment.application.port.out;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class InstallmentDetail {
    private final BigDecimal installmentsTotalAmount;
    private final BigDecimal principalTotalAmount;
    private final BigDecimal interestTotalAmount;
    private final BigDecimal taxTotalAmount;
    private final Map<String, BigDecimal> taxes;
}
