package com.lucas.bank.installment.domain;

import com.lucas.bank.loan.domain.AmortizationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class InstallmentFactory {
    public static Installment of(AmortizationType amortizationType) {

        if (amortizationType == AmortizationType.PRICE) {
            var instance = new PriceAmortization();
            instance.setLoanAmortizationType(amortizationType);
            return instance;
        } else if (amortizationType == AmortizationType.SAC) {
            var instance = new SacAmortization();
            instance.setLoanAmortizationType(amortizationType);
            return instance;
        }
        throw new UnsupportedAmortizationException(amortizationType);
    }

    public static Installment withInformation(AmortizationType type, Integer number, InstallmentState state, LocalDateTime dueDate, LocalDateTime paymentDate, BigDecimal principalAmount, BigDecimal interestAmount, BigDecimal installmentAmount, BigDecimal taxAmount, BigDecimal remainingBalance, Map<String, BigDecimal> taxComposition, BigDecimal paidPrincipal, BigDecimal paidInterest, BigDecimal paidTax) {
        var instance = of(type);

        // ToDo - check better way to map repository POJO to multiple domain entities extended from same class

        instance.setNumber(number);
        instance.setState(state);
        instance.setDueDate(dueDate);
        instance.setPaymentDate(paymentDate);
        instance.setPrincipalAmount(principalAmount);
        instance.setInterestAmount(interestAmount);
        instance.setInstallmentAmount(installmentAmount);
        instance.setTaxAmount(taxAmount);
        instance.setRemainingBalance(remainingBalance);
        instance.setTaxComposition(taxComposition);
        instance.setPaidPrincipalAmount(paidPrincipal);
        instance.setPaidInterestAmount(paidInterest);
        instance.setPaidTaxAmount(paidTax);

        return instance;
    }
}
