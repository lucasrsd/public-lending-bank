package com.lucas.bank.ledger.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.ledger.domain.Ledger;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class LedgerResponse {
    public String ledgerEntryId;
    public Long loanId;
    public Long transactionId;
    public String type;
    public String side;
    public BigDecimal amount;
    public LedgerAccountResponse account;

    public LocalDateTime date;

    public LocalDateTime bookingDate;

    public static LedgerResponse mapToResponse(Ledger ledger){
        if (ledger == null)
            return null;

        return LedgerResponse
                .builder()
                .ledgerEntryId(ledger.getLedgerEntryId())
                .loanId(ledger.getLoanId())
                .transactionId(ledger.getTransactionId())
                .type(ledger.getType())
                .side(ledger.getSide().name())
                .amount(ledger.getAmount())
                .account(LedgerAccountResponse.mapToResponse(ledger.getAccount()))
                .date(ledger.getDate())
                .bookingDate(ledger.getBookingDate())
                .build();
    }
}
