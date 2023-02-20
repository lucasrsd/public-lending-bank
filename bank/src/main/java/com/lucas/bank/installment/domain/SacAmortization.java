package com.lucas.bank.installment.domain;

import com.lucas.bank.shared.DateTimeUtil;
import com.lucas.bank.taxes.application.port.out.TaxAggregate;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.lucas.bank.shared.StaticInformation.PRECISION_SCALE;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
public class SacAmortization extends Installment {

    public SacAmortization() {

    }

    public List<Installment> calculateAmortization(Date disbursementDate, BigDecimal rate, Integer term, BigDecimal amount, TaxAggregate taxes) {
        var pmt = Pmt.sac(term, amount).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);

        List<Installment> installments = new ArrayList<>();

        var remainingBalance = new BigDecimal(amount.toString());
        var principal = pmt;

        for (int installmentNumber = 1; installmentNumber <= term; installmentNumber++) {

            var interest = remainingBalance.multiply(rate).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);

            remainingBalance = remainingBalance.subtract(principal).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);

            if (installmentNumber == term) {
                if (remainingBalance.compareTo(BigDecimal.ZERO) != 0) {
                    principal = principal.add(remainingBalance);
                    pmt = pmt.add(remainingBalance);
                    remainingBalance = remainingBalance.add(remainingBalance.negate());
                }
            }

            var dueDate = DateTimeUtil.addMonthsAndRetrieveNextBusinessDay(disbursementDate, installmentNumber);

            var tax = addTaxIfPresent(taxes, installmentNumber);
            var installmentAmount = principal.add(interest).add(tax).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);

            var installment = InstallmentFactory.withInformation(AmortizationType.SAC, installmentNumber, InstallmentState.PENDING, dueDate, null, principal, interest, installmentAmount, tax, remainingBalance, taxCompositionIfPresent(taxes, installmentNumber));

            installments.add(installment);
        }

        return installments;
    }
}
