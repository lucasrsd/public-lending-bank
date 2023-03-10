package com.lucas.bank.loan.adapter.in.contracts;

import com.lucas.bank.installment.application.port.out.InstallmentDetail;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.valueObjects.Metadata;
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

    public static ListLoanResponse mapToResponse(List<Loan> loans){
        if (loans == null)
            return ListLoanResponse.empty();

        List<LoanResponse> loanList = new ArrayList<>();

        for(Loan loan : loans){
            loanList.add(mapToResponse(loan));
        }

        return ListLoanResponse
                .builder()
                .loans(loanList)
                .metadata(Metadata.of(loanList.size()))
                .build();
    }

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

    public static InstallmentDetailResponse mapToResponse(InstallmentDetail installmentDetail){
        return InstallmentDetailResponse
                .builder()
                .installmentsTotalAmount(installmentDetail.getInstallmentsTotalAmount())
                .principalTotalAmount(installmentDetail.getPrincipalTotalAmount())
                .interestTotalAmount(installmentDetail.getInterestTotalAmount())
                .taxesTotalAmount(installmentDetail.getTaxTotalAmount())
                .taxes(installmentDetail.getTaxes())
                .build();
    }


    public static LoanOverviewResponse mapToResponse(LoanAggregate aggregate){

        var loanResponse = mapToResponse(aggregate.getLoan());
        var installmentDetailsResponse = mapToResponse(aggregate.getInstallments().getDetails());

        List<InstallmentResponse> installmentsResponse = new ArrayList<>();

        for(Installment installment : aggregate.getInstallments().getInstallments()){
            var installmentResponse = InstallmentResponse
                    .builder()
                    .number(installment.getNumber())
                    .amortizationType(installment.getLoanAmortizationType().name())
                    .state(installment.getState().name())
                    .dueDate(installment.getDueDate())
                    .paymentDate(installment.getPaymentDate())
                    .remainingBalance(installment.getRemainingBalance())
                    .taxComposition(installment.getTaxComposition())
                    .principal(PayableAmount.of(installment.getPrincipalAmount(), installment.getPaidPrincipalAmount()))
                    .interest(PayableAmount.of(installment.getInterestAmount(), installment.getPaidInterestAmount()))
                    .tax(PayableAmount.of(installment.getTaxAmount(), installment.getPaidTaxAmount()))
                    .installmentAmount(installment.getInstallmentAmount())
                    .build();
            installmentsResponse.add(installmentResponse);
        }

        return LoanOverviewResponse
                .builder()
                .loan(loanResponse)
                .installments(installmentsResponse)
                .installmentDetails(installmentDetailsResponse)
                .build();
    }

}
