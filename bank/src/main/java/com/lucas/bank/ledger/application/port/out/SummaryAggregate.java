package com.lucas.bank.ledger.application.port.out;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class SummaryAggregate {
    private final Map<String, BigDecimal> summary;
}
