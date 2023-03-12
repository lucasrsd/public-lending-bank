package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.ledger.domain.*;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class LedgerMapper {

    LedgerAccount mapAccountToDomainEntity(LedgerPOJO ledgerPOJO){
        return LedgerAccount
                .builder()
                .ledgerAccountId(ledgerPOJO.getLedgerAccountId())
                .name(ledgerPOJO.getLedgerAccountName())
                .accountType(AccountType.valueOf(ledgerPOJO.getLedgerAccountType()))
                .build();
    }

    Ledger mapToDomainEntity(LedgerPOJO ledgerPOJO){
        return Ledger
                .builder()
                .ledgerEntryId(ledgerPOJO.getLedgerTransactionEntryId())
                .loanId(ledgerPOJO.getLoanId())
                .transactionId(ledgerPOJO.getLedgerTransactionId())
                .type(ledgerPOJO.getLedgerTransactionType())
                .side(Side.valueOf(ledgerPOJO.getLedgerTransactionSide()))
                .amount(ledgerPOJO.getLedgerTransactionAmount())
                .ledger(mapAccountToDomainEntity(ledgerPOJO))
                .date(DateTimeUtil.from(ledgerPOJO.getLedgerDate()))
                .bookingDate(DateTimeUtil.from(ledgerPOJO.getLedgerBookingDate()))
                .build();
    }

    LedgerPOJO mapToPOJO(Ledger ledger){
        return LedgerPOJO
                .builder()
                .ledgerTransactionEntryId(ledger.getLedgerEntryId())
                .loanId(ledger.getLoanId())
                .ledgerTransactionId(ledger.getTransactionId())
                .ledgerTransactionType(ledger.getType())
                .ledgerTransactionSide(ledger.getSide().name())
                .ledgerTransactionAmount(ledger.getAmount())
                .ledgerAccountId(ledger.getLedger().getLedgerAccountId())
                .ledgerAccountType(ledger.getLedger().getAccountType().name())
                .ledgerAccountName(ledger.getLedger().getName())
                .ledgerDate(DateTimeUtil.to(ledger.getDate()))
                .ledgerBookingDate(DateTimeUtil.to(ledger.getBookingDate()))
                .build();
    }
}