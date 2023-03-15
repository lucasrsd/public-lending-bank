package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.shared.adapters.AtomicCounter;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.interest.domain.InterestFrequency;
import com.lucas.bank.loan.domain.*;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    Loan mapToDomainEntity(LoanPOJO loanPOJO){
        return Loan
                .builder()
                .loanId(loanPOJO.getLoanId())
                .accountId(loanPOJO.getAccountId())
                .type(AmortizationType.valueOf(loanPOJO.getType()))
                .amount(loanPOJO.getAmount())
                .state(LoanState.valueOf(loanPOJO.getLoanState()))
                .term(loanPOJO.getTerm())
                .interest(Interest.of(loanPOJO.getInterestRate(), InterestFrequency.valueOf(loanPOJO.getInterestFrequency())))
                .creationDate(DateTimeUtil.from(loanPOJO.getCreationDate()))
                .disbursementDate(DateTimeUtil.from(loanPOJO.getDisbursementDate()))
                .lastAccrualDate(DateTimeUtil.from(loanPOJO.getLastAccrualDate()))
                .accruedInterest(loanPOJO.getAccruedInterest())
                .additionalInformation(loanPOJO.getAdditionalInformation())
                .batchBlock(loanPOJO.getBatchBlock())
                .build();
    }

    LoanPOJO mapToPOJO(Loan loan) {
        return LoanPOJO
                .builder()
                .loanId(loan.getLoanId())
                .type(loan.getType().name())
                .accountId(loan.getAccountId())
                .amount(loan.getAmount())
                .loanState(loan.getState().name())
                .term(loan.getTerm())
                .interestRate(loan.getInterest().getRatePercentage())
                .interestFrequency(loan.getInterest().getFrequency().name())
                .creationDate(DateTimeUtil.to(loan.getCreationDate()))
                .disbursementDate(DateTimeUtil.to(loan.getDisbursementDate()))
                .lastAccrualDate(DateTimeUtil.to(loan.getLastAccrualDate()))
                .accruedInterest(loan.getAccruedInterest())
                .additionalInformation(loan.getAdditionalInformation())
                .batchBlock(loan.getBatchBlock())
                .build();
    }
}
