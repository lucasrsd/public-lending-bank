package com.lucas.bank.installment.domain;

import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.tax.application.port.out.TaxAggregate;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public abstract class Installment {
    private Integer number;
    private AmortizationType loanAmortizationType;
    private InstallmentState state;
    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;

    private BigDecimal remainingBalance;

    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal installmentAmount;
    private BigDecimal taxAmount;
    private Map<String, BigDecimal> taxComposition;

    private BigDecimal paidPrincipalAmount;
    private BigDecimal paidInterestAmount;
    private BigDecimal paidTaxAmount;

    public Installment() {
    }

    public abstract List<Installment> calculateAmortization(LocalDateTime disbursementDate, BigDecimal rate, Integer term, BigDecimal amount, TaxAggregate taxes);

    protected BigDecimal addTaxIfPresent(TaxAggregate taxes, Integer installmentNumber) {
        if (taxes != null && taxes.getTaxAmount().containsKey(installmentNumber)) {
            // Distributed tax
            //return taxes.getTaxAmount().get(installmentNumber);
            // Equal installment amount
            return taxes.getTotalTax().divide(new BigDecimal(taxes.getTaxAmount().size()), RoundingMode.HALF_DOWN).setScale(StaticInformation.CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);
        }
        return BigDecimal.ZERO;
    }

    protected Map<String, BigDecimal> taxCompositionIfPresent(TaxAggregate taxes, Integer installmentNumber) {
        if (taxes != null && taxes.getTaxAmount().containsKey(installmentNumber)) {
            return taxes.getComposition().get(installmentNumber);
        }
        return null;
    }

    protected void formatToFinancialTransaction(){
        setRemainingBalance(getRemainingBalance().setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE));
        setPrincipalAmount(getPrincipalAmount().setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE));
        setInterestAmount(getInterestAmount().setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE));
        setTaxAmount(getTaxAmount().setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE));
        setInstallmentAmount(getInstallmentAmount().setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE));
    }

}
