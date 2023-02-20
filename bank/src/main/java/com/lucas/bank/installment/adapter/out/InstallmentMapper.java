package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.shared.DateTimeUtil;
import com.lucas.bank.installment.domain.*;
import org.springframework.stereotype.Component;

@Component
class InstallmentMapper {

    Installment mapToDomainEntity(InstallmentPOJO installmentPOJO) {

        return InstallmentFactory.withInformation(AmortizationType.valueOf(installmentPOJO.getAmortizationType()),
                installmentPOJO.getNumber(),
                InstallmentState.valueOf(installmentPOJO.getInstallmentState()),
                DateTimeUtil.from(installmentPOJO.getDueDate()),
                DateTimeUtil.from(installmentPOJO.getPaymentDate()),
                installmentPOJO.getPrincipalAmount(),
                installmentPOJO.getInterestAmount(),
                installmentPOJO.getInstallmentAmount(),
                installmentPOJO.getTaxAmount(),
                installmentPOJO.getRemainingBalance(),
                installmentPOJO.getTaxComposition());
    }

    InstallmentPOJO mapToPOJO(AmortizationType amortizationType, Long loanId, Installment installment) {

        return InstallmentPOJO
                .builder()
                .loanId(loanId)
                .amortizationType(amortizationType.name())
                .number(installment.getNumber())
                .installmentState(installment.getState().name())
                .dueDate(DateTimeUtil.to(installment.getDueDate()))
                .paymentDate(DateTimeUtil.to(installment.getPaymentDate()))
                .principalAmount(installment.getPrincipalAmount())
                .interestAmount(installment.getInterestAmount())
                .installmentAmount(installment.getInstallmentAmount())
                .taxAmount(installment.getTaxAmount())
                .remainingBalance(installment.getRemainingBalance())
                .taxComposition(installment.getTaxComposition())
                .build();
    }

}
