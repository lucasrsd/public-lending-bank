package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.domain.JournalEntry;

public interface LoanAccrualLedgerUseCase {
    LedgerAggregate execute(LoanAccrualLedgerCommand command);
}
