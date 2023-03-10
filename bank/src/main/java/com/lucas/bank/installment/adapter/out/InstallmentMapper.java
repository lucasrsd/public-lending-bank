package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.installment.domain.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class InstallmentMapper {

    List<Installment> mapToDomainEntity(List<InstallmentPOJO> installmentsPOJO) {

        List<Installment> installments = new ArrayList<>();

        for(InstallmentPOJO installmentPOJO : installmentsPOJO) {

            var installment =  InstallmentFactory.withInformation(AmortizationType.valueOf(installmentPOJO.getAmortizationType()),
                    installmentPOJO.getNumber(),
                    InstallmentState.valueOf(installmentPOJO.getInstallmentState()),
                    DateTimeUtil.from(installmentPOJO.getDueDate()),
                    DateTimeUtil.from(installmentPOJO.getPaymentDate()),
                    installmentPOJO.getPrincipalAmount(),
                    installmentPOJO.getInterestAmount(),
                    installmentPOJO.getInstallmentAmount(),
                    installmentPOJO.getTaxAmount(),
                    installmentPOJO.getRemainingBalance(),
                    installmentPOJO.getTaxComposition(),
                    installmentPOJO.getPaidPrincipalAmount(),
                    installmentPOJO.getPaidInterestAmount(),
                    installmentPOJO.getPaidTaxAmount());

            installments.add(installment);
        }

        return installments;
    }

    InstallmentDataPOJO mapToPOJO(AmortizationType amortizationType, Long loanId, List<Installment> installments) {

        List<InstallmentPOJO> installmentsPOJO = new ArrayList<>();

        for(Installment installment : installments){
            var installmentPOJO  = InstallmentPOJO
                    .builder()
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
                    .paidPrincipalAmount(installment.getPaidPrincipalAmount())
                    .paidInterestAmount(installment.getPaidInterestAmount())
                    .paidTaxAmount(installment.getPaidTaxAmount())
                    .build();

            installmentsPOJO.add(installmentPOJO);
        }

        InstallmentDataPOJO installmentDataPOJO = InstallmentDataPOJO
                .builder()
                .loanId(loanId)
                .installments(installmentsPOJO)
                .build();

        return installmentDataPOJO;
    }

}
