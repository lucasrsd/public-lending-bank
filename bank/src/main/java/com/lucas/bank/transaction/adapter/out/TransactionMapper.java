package com.lucas.bank.transaction.adapter.out;

import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.transaction.domain.Transaction;
import com.lucas.bank.transaction.domain.TransactionType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionMapper {

    Transaction mapToDomainEntity(TransactionPOJO transactionPOJO){
        return Transaction
                .builder()
                .transactionId(transactionPOJO.getTransactionId())
                .loanId(transactionPOJO.getLoanId())
                .date(DateTimeUtil.from(transactionPOJO.getDate()))
                .amount(transactionPOJO.getAmount())
                .type(TransactionType.valueOf(transactionPOJO.getType()))
                .build();
    }

    TransactionPOJO mapToPOJO(Transaction transaction) {

        Long transactionId = transaction.getTransactionId();

        if (transactionId == null){
            transactionId =  new Date().getTime();
        }

        return TransactionPOJO
                .builder()
                .transactionId(transactionId)
                .loanId(transaction.getLoanId())
                .date(DateTimeUtil.to(transaction.getDate()))
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .build();
    }
}
