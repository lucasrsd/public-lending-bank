package com.lucas.bank.installment.domain;

import com.lucas.bank.taxes.application.port.out.TaxAggregate;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public abstract class Installment {
    private Integer number;
    private AmortizationType amortizationType;
    private InstallmentState state;
    private Date dueDate;
    private Date paymentDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal installmentAmount;
    private BigDecimal taxAmount;
    private BigDecimal remainingBalance;
    private Map<String, BigDecimal> taxComposition;

    public Installment() {
    }

    public abstract List<Installment> calculateAmortization(Date disbursementDate, BigDecimal rate, Integer term, BigDecimal amount, TaxAggregate taxes);

    protected BigDecimal addTaxIfPresent(TaxAggregate taxes, Integer installmentNumber) {
        if (taxes != null && taxes.getTaxAmount().containsKey(installmentNumber)) {
            return taxes.getTaxAmount().get(installmentNumber);
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
