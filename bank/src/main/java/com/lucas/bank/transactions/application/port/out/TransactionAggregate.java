package com.lucas.bank.transactions.application.port.out;

import com.lucas.bank.transactions.domain.Transaction;
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
