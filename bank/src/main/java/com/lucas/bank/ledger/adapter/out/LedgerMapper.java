package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.ledger.domain.*;
import com.lucas.bank.shared.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LedgerMapper {

    Ledger mapToDomainEntity(LedgerPOJO ledgerPOJO){
        return Ledger
                .builder()
                .ledgerEntryId(ledgerPOJO.getLedgerEntryId())
                .loanId(ledgerPOJO.getLoanId())
                .transactionType(ledgerPOJO.getTransactionType())
                .transactionDate(DateTimeUtil.from(ledgerPOJO.getTransactionDate()))
                .debit(mapToDomainEntity(ledgerPOJO.getDebit()))
                .credit(mapToDomainEntity(ledgerPOJO.getCredit()))
                .build();
    }

    LedgerPOJO mapToPOJO(Ledger ledger) {

        return LedgerPOJO
                .builder()
                .ledgerEntryId(ledger.getLedgerEntryId())
                .loanId(ledger.getLoanId())
                .transactionType(ledger.getTransactionType())
                .transactionDate(DateTimeUtil.to(ledger.getTransactionDate()))
                .debit(mapToPOJO(ledger.getDebit()))
                .credit(mapToPOJO(ledger.getCredit()))
                .build();
    }

    JournalTransaction mapToDomainEntity(JournalTransactionPOJO journalTransactionPOJO){
        var ledgerAccount = LedgerAccount
                .builder()
                .accountType(AccountType.valueOf(journalTransactionPOJO.getAccountType()))
                .name(journalTransactionPOJO.getLedgerAccountName())
                .ledgerAccountId(journalTransactionPOJO.getLedgerAccountId())
                .build();

        return JournalTransaction
                .builder()
                .journalTransactionId(journalTransactionPOJO.getJournalTransactionId())
                .transactionDate(DateTimeUtil.from(journalTransactionPOJO.getTransactionDate()))
                .bookingDate(DateTimeUtil.from(journalTransactionPOJO.getBookingDate()))
                .operationType(OperationType.valueOf(journalTransactionPOJO.getOperationType()))
                .amount(journalTransactionPOJO.getAmount())
                .ledgerAccount(ledgerAccount)
                .build();
    }

    JournalTransactionPOJO mapToPOJO(JournalTransaction journalTransaction){
        return JournalTransactionPOJO
                .builder()
                .journalTransactionId(journalTransaction.getJournalTransactionId())
                .transactionDate(DateTimeUtil.to(journalTransaction.getTransactionDate()))
                .bookingDate(DateTimeUtil.to(journalTransaction.getBookingDate()))
                .operationType(journalTransaction.getOperationType().name())
                .amount(journalTransaction.getAmount())
                .ledgerAccountId(journalTransaction.getLedgerAccount().getLedgerAccountId())
                .ledgerAccountName(journalTransaction.getLedgerAccount().getName())
                .accountType(journalTransaction.getLedgerAccount().getAccountType().name())
                .build();
    }
}