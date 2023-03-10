package com.lucas.bank.transaction.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Transaction {
    private final Long transactionId;
    private final Long loanId;
    private final Date date;
    private final TransactionType type;
    private final BigDecimal amount;

    public static Transaction withoutId(TransactionType type, Long loanId, BigDecimal amount){
        return Transaction
                .builder()
                .loanId(loanId)
                .date(new Date())
                .type(type)
                .amount(amount)
                .build();
    }
}