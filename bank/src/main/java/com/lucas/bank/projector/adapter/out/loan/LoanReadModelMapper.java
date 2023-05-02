package com.lucas.bank.projector.adapter.out.loan;

import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.projector.adapter.out.account.AccountReadModel;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class LoanReadModelMapper {

    public LoanReadModel mapToDomainEntity(LoanPOJO loan) {
        return LoanReadModel
                .builder()
                .loanId(loan.getLoanId())
                .type(loan.getType())
                .accountId(loan.getAccountId())
                .amount(loan.getAmount())
                .loanState(loan.getLoanState())
                .term(loan.getTerm())
                .interestRate(loan.getInterestRate())
                .interestFrequency(loan.getInterestFrequency())
                .creationDate(DateTimeUtil.from(loan.getCreationDate()))
                .disbursementDate(DateTimeUtil.from(loan.getDisbursementDate()))
                .lastAccrualDate(DateTimeUtil.from(loan.getLastAccrualDate()))
                .accruedInterest(loan.getAccruedInterest())
                .batchBlock(loan.getBatchBlock())
                .build();
    }
}