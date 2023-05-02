package com.lucas.bank.ledger.application.port.out;

import java.time.LocalDateTime;
import java.util.List;


public interface LoadLedgerSummaryPort {

    List<LedgerSummaryAggregate> summarizeLedgers(LocalDateTime startDate, LocalDateTime endDate);
}
