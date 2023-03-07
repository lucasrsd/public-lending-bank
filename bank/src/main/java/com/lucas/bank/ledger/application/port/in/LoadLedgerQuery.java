package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.SummaryAggregate;

public interface LoadLedgerQuery {

    SummaryAggregate summary();

}