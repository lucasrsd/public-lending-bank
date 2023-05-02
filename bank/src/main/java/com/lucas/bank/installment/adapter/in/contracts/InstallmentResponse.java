package com.lucas.bank.installment.adapter.in.contracts;

import com.lucas.bank.installment.application.port.out.InstallmentDetail;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.valueObjects.PayableAmount;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class InstallmentResponse {

    public Integer number;
    public String amortizationType;
    public String state;

    public LocalDateTime dueDate;

    public LocalDateTime paymentDate;

    public PayableAmount principal;
    public PayableAmount interest;
    public PayableAmount tax;
    public BigDecimal installmentAmount;

    public BigDecimal remainingBalance;
    public Map<String, BigDecimal> taxComposition;

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

    public static List<InstallmentResponse> mapToResponse(List<Installment> installments){
        List<InstallmentResponse> installmentsResponse = new ArrayList<>();

        for(Installment installment : installments){
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

        return installmentsResponse;
    }

}
