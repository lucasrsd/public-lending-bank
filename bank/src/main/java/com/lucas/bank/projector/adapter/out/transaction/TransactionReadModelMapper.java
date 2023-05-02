package com.lucas.bank.projector.adapter.out.transaction;

import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.transaction.adapter.out.TransactionPOJO;
import org.springframework.stereotype.Component;

@Component
public class TransactionReadModelMapper {

    public TransactionReadModel mapToDomainEntity(TransactionPOJO transaction) {
        return TransactionReadModel
                .builder()
                .transactionId(transaction.getTransactionId())
                .loanId(transaction.getLoanId())
                .date(DateTimeUtil.from(transaction.getDate()))
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .build();
    }
}