package com.lucas.bank.tax.application.port.out;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class TaxAggregate {
    private final Map<Integer, BigDecimal> taxAmount;
    private final Map<Integer, Map<String, BigDecimal>> composition;
    private final BigDecimal totalTax;
}
