package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.Ledger;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class LedgerAggregate {
    private final Ledger ledger;
}
