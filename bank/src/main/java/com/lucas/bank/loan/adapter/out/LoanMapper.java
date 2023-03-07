package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.shared.DateTimeUtil;
import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.interest.domain.InterestFrequency;
import com.lucas.bank.loan.domain.*;
import org.springframework.stereotype.Component;

import java.util.Date;

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
                .additionalInformation(loanPOJO.getAdditionalInformation())
                .build();
    }

    LoanPOJO mapToPOJO(Loan loan) {

        Long loanId = loan.getLoanId();

        if (loanId == null){
            loanId =  new Date().getTime();
        }
        return LoanPOJO
                .builder()
                .loanId(loanId)
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
                .additionalInformation(loan.getAdditionalInformation())
                .build();
    }
}
