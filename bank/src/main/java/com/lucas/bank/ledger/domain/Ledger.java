package com.lucas.bank.ledger.domain;

import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final LedgerAccount account;
    private final LocalDateTime date;
    private final LocalDateTime bookingDate;

    public static Ledger forCredit(Long loanId, Long transactionId, String type, BigDecimal amount, LedgerAccount account){
        return Ledger
                .builder()
                .ledgerEntryId(UUID.randomUUID().toString())
                .loanId(loanId)
                .transactionId(transactionId)
                .type(type)
                .side(Side.CREDIT)
                .amount(amount)
                .account(account)
                .date(DateTimeUtil.nowWithTimeZone())
                .bookingDate(DateTimeUtil.nowWithTimeZone())
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
                .account(account)
                .date(DateTimeUtil.nowWithTimeZone())
                .bookingDate(DateTimeUtil.nowWithTimeZone())
                .build();
    }
}
