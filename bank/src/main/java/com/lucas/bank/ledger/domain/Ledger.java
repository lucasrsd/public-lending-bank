package com.lucas.bank.ledger.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Ledger {
    private final String ledgerEntryId;
    private final Long loanId;
    private final Long transactionId;
    private final String type;
    private final Side side;
    private final BigDecimal amount;
    private final LedgerAccount ledger;
    private final Date date;
    private final Date bookingDate;

    public static Ledger forCredit(Long loanId, Long transactionId, String type, BigDecimal amount, LedgerAccount account){
        return Ledger
                .builder()
                .ledgerEntryId(UUID.randomUUID().toString())
                .loanId(loanId)
                .transactionId(transactionId)
                .type(type)
                .side(Side.CREDIT)
                .amount(amount)
                .ledger(account)
                .date(new Date())
                .bookingDate(new Date())
                .build();
    }

    public static Ledger forDebit(Long loanId, Long transactionId, String type, BigDecimal amount, LedgerAccount account){
        return Ledger
                .builder()
                .ledgerEntryId(UUID.randomUUID().toString())
                .loanId(loanId)
                .transactionId(transactionId)
                .type(type)
                .side(Side.DEBIT)
                .amount(amount.negate())
                .ledger(account)
                .date(new Date())
                .bookingDate(new Date())
                .build();
    }
}
