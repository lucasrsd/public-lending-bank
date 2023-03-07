package com.lucas.bank.installment.application.service;

import com.lucas.bank.installment.application.port.in.InstallmentRepaymentUseCase;
import com.lucas.bank.installment.application.port.out.InstallmentRepaymentAggregate;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.installment.domain.InstallmentState;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class InstallmentRepaymentService implements InstallmentRepaymentUseCase {

    @Override
    public InstallmentRepaymentAggregate calculateRepayment(List<Installment> installments, BigDecimal amount) {

        BigDecimal remainingPaymentAmount = new BigDecimal(amount.toString());
        BigDecimal affectedPrincipal = BigDecimal.ZERO;
        BigDecimal affectedInterest = BigDecimal.ZERO;
        BigDecimal affectedTax = BigDecimal.ZERO;

        if (installments.stream().allMatch(i -> i.getState().equals(InstallmentState.PAID)))
            throw new RuntimeException("All installments are paid, operation cancelled.");

        var installmentsPendingPaymentAmount = sumTotalPendingPaymentAmount(installments);

        if (amount.compareTo(installmentsPendingPaymentAmount) > 0)
            throw new RuntimeException("Repayment amount greater than the total pending: " + installmentsPendingPaymentAmount);

        Collections.sort(installments, Comparator.comparing(Installment::getNumber));

        for (Installment installment : installments) {

            if (installment.getState().equals(InstallmentState.PAID)) continue;

            // ToDo - check how to better split payments (proportional payments when can't affect principal)

            // Interest

            BigDecimal interestApplied = tryDeductInterest(installment, remainingPaymentAmount);
            affectedInterest = affectedInterest.add(interestApplied);
            remainingPaymentAmount = remainingPaymentAmount.subtract(interestApplied);

            if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) break;

            // Tax

            BigDecimal taxApplied = tryDeductTax(installment, remainingPaymentAmount);
            affectedTax = affectedTax.add(taxApplied);
            remainingPaymentAmount = remainingPaymentAmount.subtract(taxApplied);

            if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) break;

            // Principal

            BigDecimal principalApplied = tryDeductPrincipal(installment, remainingPaymentAmount);
            affectedPrincipal = affectedPrincipal.add(principalApplied);
            remainingPaymentAmount = remainingPaymentAmount.subtract(principalApplied);

            if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) break;
        }

        for (Installment installment : installments) {
            checkInstallmentState(installment);
        }

        return InstallmentRepaymentAggregate
                .builder()
                .installments(installments)
                .affectedInterest(affectedInterest)
                .affectedPrincipal(affectedPrincipal)
                .affectedTax(affectedTax)
                .build();
    }

    private BigDecimal tryDeductPrincipal(Installment installment, BigDecimal remainingPaymentAmount) {
        if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        var remainingPrincipal = installment.getPrincipalAmount().subtract(installment.getPaidPrincipalAmount());

        BigDecimal toApply = BigDecimal.ZERO;

        if (remainingPaymentAmount.compareTo(remainingPrincipal) > 0)
            toApply = remainingPrincipal;
        else
            toApply = remainingPaymentAmount;

        installment.setPaidPrincipalAmount(installment.getPaidPrincipalAmount().add(toApply));
        return toApply;
    }

    private BigDecimal tryDeductInterest(Installment installment, BigDecimal remainingPaymentAmount) {
        if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        var remainingInterest = installment.getInterestAmount().subtract(installment.getPaidInterestAmount());

        BigDecimal toApply = BigDecimal.ZERO;

        if (remainingPaymentAmount.compareTo(remainingInterest) > 0) toApply = remainingInterest;
        else toApply = remainingPaymentAmount;

        installment.setPaidInterestAmount(installment.getPaidInterestAmount().add(toApply));
        return toApply;
    }

    private BigDecimal tryDeductTax(Installment installment, BigDecimal remainingPaymentAmount) {
        if (remainingPaymentAmount.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        var remainingTax = installment.getTaxAmount().subtract(installment.getPaidTaxAmount());

        BigDecimal toApply = BigDecimal.ZERO;

        if (remainingPaymentAmount.compareTo(remainingTax) > 0) toApply = remainingTax;
        else toApply = remainingPaymentAmount;

        installment.setPaidTaxAmount(installment.getPaidTaxAmount().add(toApply));
        return toApply;
    }

    private void checkInstallmentState(Installment installment) {

        if (installment.getState().equals(InstallmentState.PAID)) return;

        var principalFullyPaid = installment.getPrincipalAmount().compareTo(installment.getPaidPrincipalAmount()) == 0;
        var interestFullyPaid = installment.getInterestAmount().compareTo(installment.getPaidInterestAmount()) == 0;
        var taxesFullyPaid = installment.getTaxAmount().compareTo(installment.getPaidTaxAmount()) == 0;

        if (principalFullyPaid && interestFullyPaid && taxesFullyPaid)
        {
            installment.setPaymentDate(new Date());
            installment.setState(InstallmentState.PAID);
        }
    }

    private BigDecimal sumTotalPendingPaymentAmount(List<Installment> installments) {

        var pendingSum = BigDecimal.ZERO;

        for (Installment installment : installments) {
            pendingSum = pendingSum.add(installment.getPrincipalAmount().subtract(installment.getPaidPrincipalAmount()));
            pendingSum = pendingSum.add(installment.getInterestAmount().subtract(installment.getPaidInterestAmount()));
            pendingSum = pendingSum.add(installment.getTaxAmount().subtract(installment.getPaidTaxAmount()));
        }
        return pendingSum;
    }
}
