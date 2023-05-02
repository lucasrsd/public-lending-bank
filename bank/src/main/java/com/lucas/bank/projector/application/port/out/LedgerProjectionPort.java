package com.lucas.bank.projector.application.port.out;

import com.lucas.bank.ledger.adapter.out.LedgerPOJO;

public interface LedgerProjectionPort {
    void project(LedgerPOJO ledgerPOJO);
}
