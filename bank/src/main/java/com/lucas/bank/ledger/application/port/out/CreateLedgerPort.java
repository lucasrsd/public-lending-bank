package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

public interface CreateLedgerPort {
    String createEntry(Ledger ledger, UnitOfWork unitOfWork);
}
