package com.lucas.bank.transaction.adapter.in.contracts;
import com.lucas.bank.installment.adapter.in.contracts.InstallmentResponse;
import com.lucas.bank.installment.application.port.out.InstallmentRepaymentAggregate;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class RepayLoanResponse {
    public BigDecimal affectedPrincipal;
    public BigDecimal affectedInterest;
    public BigDecimal affectedTax;
    public List<InstallmentResponse> installments;

    public static RepayLoanResponse mapToResponse(InstallmentRepaymentAggregate installmentRepaymentAggregate){
        if (installmentRepaymentAggregate == null) return null;

        return RepayLoanResponse
                .builder()
                .affectedPrincipal(installmentRepaymentAggregate.getAffectedPrincipal())
                .affectedInterest(installmentRepaymentAggregate.getAffectedInterest())
                .affectedTax(installmentRepaymentAggregate.getAffectedTax())
                .installments(InstallmentResponse.mapToResponse(installmentRepaymentAggregate.getInstallments()))
                .build();
    }
}
