package com.lucas.bank.loan.adapter.in.contracts;

import com.lucas.bank.installment.adapter.in.contracts.InstallmentDetailResponse;
import com.lucas.bank.installment.adapter.in.contracts.InstallmentResponse;
import com.lucas.bank.installment.application.port.out.InstallmentDetail;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.valueObjects.PayableAmount;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class LoanOverviewResponse {

    public LoanResponse loan;
    public List<InstallmentResponse> installments;
    public InstallmentDetailResponse installmentDetails;

    public static LoanResponse mapToResponse(Loan loan){
        var loanResponse =  LoanResponse
                .builder()
                .accountId(loan.getAccountId())
                .loanId(loan.getLoanId())
                .type(loan.getType().name())
                .amount(loan.getAmount())
                .state(loan.getState().name())
                .term(loan.getTerm())
                .interestRate(loan.getInterest().getRatePercentage())
                .interestFrequency(loan.getInterest().getFrequency().name())
                .creationDate(loan.getCreationDate())
                .disbursementDate(loan.getDisbursementDate())
                .lastAccrualDate(loan.getLastAccrualDate())
                .accruedInterest(loan.getAccruedInterest())
                .additionalInformation(loan.getAdditionalInformation())
                .build();

        return loanResponse;
    }

    public static LoanOverviewResponse mapToResponse(LoanAggregate aggregate){

        var loanResponse = mapToResponse(aggregate.getLoan());
        var installmentDetailsResponse = InstallmentResponse.mapToResponse(aggregate.getInstallments().getDetails());
        var installmentsResponse = InstallmentResponse.mapToResponse(aggregate.getInstallments().getInstallments());

        return LoanOverviewResponse
                .builder()
                .loan(loanResponse)
                .installments(installmentsResponse)
                .installmentDetails(installmentDetailsResponse)
                .build();
    }

}
