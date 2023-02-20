package com.lucas.bank.loan.adapter.in.contracts;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class CreateLoanResponse {

    public LoanResponse loan;
    public List<InstallmentResponse> installments;
    public List<String> installmentDetails;

    public static CreateLoanResponse mapToResponse(LoanAggregate aggregate){
        var loanResponse =  LoanResponse
                .builder()
                .accountId(aggregate.getLoan().getAccountId())
                .loanId(aggregate.getLoan().getLoanId())
                .type(aggregate.getLoan().getType().name())
                .amount(aggregate.getLoan().getAmount())
                .state(aggregate.getLoan().getState().name())
                .term(aggregate.getLoan().getTerm())
                .interestRate(aggregate.getLoan().getInterest().getRatePercentage())
                .interestFrequency(aggregate.getLoan().getInterest().getFrequency().name())
                .creationDate(aggregate.getLoan().getCreationDate())
                .disbursementDate(aggregate.getLoan().getDisbursementDate())
                .lastAccrualDate(aggregate.getLoan().getLastAccrualDate())
                .additionalInformation(aggregate.getLoan().getAdditionalInformation())
                .build();

        var installmentDetailsResponse = aggregate.getInstallments().getDetails();

        List<InstallmentResponse> installmentsResponse = new ArrayList<>();

        for(Installment installment : aggregate.getInstallments().getInstallments()){
            var installmentResponse = InstallmentResponse
                    .builder()
                    .number(installment.getNumber())
                    .amortizationType(installment.getAmortizationType().name())
                    .state(installment.getState().name())
                    .dueDate(installment.getDueDate())
                    .paymentDate(installment.getPaymentDate())
                    .principalAmount(installment.getPrincipalAmount())
                    .interestAmount(installment.getInterestAmount())
                    .installmentAmount(installment.getInstallmentAmount())
                    .taxAmount(installment.getTaxAmount())
                    .remainingBalance(installment.getRemainingBalance())
                    .taxComposition(installment.getTaxComposition())
                    .build();
            installmentsResponse.add(installmentResponse);
        }

        return CreateLoanResponse
                .builder()
                .loan(loanResponse)
                .installments(installmentsResponse)
                .installmentDetails(installmentDetailsResponse)
                .build();
    }

}
