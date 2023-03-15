package com.lucas.bank.transaction.domain;

import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Transaction {
    private Long transactionId;
    private Long loanId;
    private LocalDateTime date;
    private TransactionType type;
    private BigDecimal amount;

    public static Transaction withoutId(TransactionType type, Long loanId, BigDecimal amount){
        return Transaction
                .builder()
                .loanId(loanId)
                .date(DateTimeUtil.nowWithTimeZone())
                .type(type)
                .amount(amount)
                .build();
    }
}