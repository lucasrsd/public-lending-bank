package com.lucas.bank.transactions.adapter.out;

import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.interest.domain.InterestFrequency;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.loan.domain.LoanState;
import com.lucas.bank.shared.DateTimeUtil;
import com.lucas.bank.transactions.domain.Transaction;
import com.lucas.bank.transactions.domain.TransactionType;
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
