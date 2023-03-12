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
    private Long transactionId;
    private Long loanId;
    private Date date;
    private TransactionType type;
    private BigDecimal amount;

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