package com.lucas.bank.projector.adapter.out.ledger;

import com.lucas.bank.ledger.adapter.out.LedgerPOJO;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;


@Component
public class LedgerReadModelMapper {

    public LedgerReadModel mapToDomainEntity(LedgerPOJO ledgerPOJO) {
        return LedgerReadModel
                .builder()
                .ledgerTransactionEntryId(ledgerPOJO.getLedgerTransactionEntryId())
                .loanId(ledgerPOJO.getLoanId())
                .ledgerTransactionId(ledgerPOJO.getLedgerTransactionId())
                .ledgerTransactionType(ledgerPOJO.getLedgerTransactionType())
                .ledgerTransactionSide(ledgerPOJO.getLedgerTransactionSide())
                .ledgerTransactionAmount(ledgerPOJO.getLedgerTransactionAmount())
                .ledgerAccountId(ledgerPOJO.getLedgerAccountId())
                .ledgerAccountName(ledgerPOJO.getLedgerAccountName())
                .ledgerAccountType(ledgerPOJO.getLedgerAccountType())
                .ledgerDate(DateTimeUtil.from(ledgerPOJO.getLedgerDate()))
                .ledgerBookingDate(DateTimeUtil.from(ledgerPOJO.getLedgerBookingDate()))
                .build();
    }
}