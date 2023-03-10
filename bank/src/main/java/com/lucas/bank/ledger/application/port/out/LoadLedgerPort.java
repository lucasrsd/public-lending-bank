package com.lucas.bank.ledger.application.port.out;

import java.math.BigDecimal;
import java.util.Map;


public interface LoadLedgerPort {
    Map<String, BigDecimal> summarizeLedgerBalance();
}
