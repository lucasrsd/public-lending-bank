package com.lucas.bank.transaction.application.port.out;

import com.lucas.bank.transaction.domain.Transaction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class TransactionAggregate {
    private final List<Transaction> transactions;
}
