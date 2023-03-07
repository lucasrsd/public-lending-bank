package com.lucas.bank.installment.domain;

import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.StaticInformation;
import com.lucas.bank.taxes.application.port.out.TaxAggregate;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public abstract class Installment {
    private Integer number;
    private AmortizationType loanAmortizationType;
    private InstallmentState state;
    private Date dueDate;
    private Date paymentDate;

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

    public abstract List<Installment> calculateAmortization(Date disbursementDate, BigDecimal rate, Integer term, BigDecimal amount, TaxAggregate taxes);

    protected BigDecimal addTaxIfPresent(TaxAggregate taxes, Integer installmentNumber) {
        if (taxes != null && taxes.getTaxAmount().containsKey(installmentNumber)) {
            // Distributed tax
            //return taxes.getTaxAmount().get(installmentNumber);
            // Equal installment amount
            return taxes.getTotalTax().divide(new BigDecimal(taxes.getTaxAmount().size()), RoundingMode.HALF_DOWN).setScale(StaticInformation.PRECISION_SCALE, RoundingMode.HALF_DOWN);
        }
        return BigDecimal.ZERO;
    }

    protected Map<String, BigDecimal> taxCompositionIfPresent(TaxAggregate taxes, Integer installmentNumber) {
        if (taxes != null && taxes.getTaxAmount().containsKey(installmentNumber)) {
            return taxes.getComposition().get(installmentNumber);
        }
        return null;
    }

}
