package com.lucas.bank.installment.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class InstallmentFactory {
    public static Installment of(AmortizationType amortizationType) {

        if (amortizationType == AmortizationType.PRICE) {
            var instance = new PriceAmortization();
            instance.setAmortizationType(amortizationType);
            return instance;
        } else if (amortizationType == AmortizationType.SAC) {
            var instance = new SacAmortization();
            instance.setAmortizationType(amortizationType);
            return instance;
        }
        throw new RuntimeException("Calculation type not supported");
    }

    public static Installment withInformation(AmortizationType type, Integer number, InstallmentState state, Date dueDate, Date paymentDate, BigDecimal principalAmount, BigDecimal interestAmount, BigDecimal installmentAmount, BigDecimal taxAmount, BigDecimal remainingBalance, Map<String, BigDecimal> taxComposition) {
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

        return instance;
    }
}
