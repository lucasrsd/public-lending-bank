package com.lucas.bank.installment.domain;

import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.tax.application.port.out.TaxAggregate;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lucas.bank.shared.staticInformation.StaticInformation.CALCULATION_PRECISION_SCALE;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
public class PriceAmortization extends Installment {

    public PriceAmortization() {

    }

    public List<Installment> calculateAmortization(LocalDateTime disbursementDate, BigDecimal rate, Integer term, BigDecimal amount, TaxAggregate taxes) {
        var pmt = Pmt.price(rate, term, amount.negate()).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);

        List<Installment> installments = new ArrayList<>();

        var remainingBalance = new BigDecimal(amount.toString());

        for (int installmentNumber = 1; installmentNumber <= term; installmentNumber++) {

            var interest = remainingBalance.multiply(rate).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);

            var principal = pmt.subtract(interest).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);

            remainingBalance = remainingBalance.subtract(principal).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);

            if (installmentNumber == term) {
                if (remainingBalance.compareTo(BigDecimal.ZERO) != 0) {
                    principal = principal.add(remainingBalance);
                    pmt = pmt.add(remainingBalance);
                    remainingBalance = remainingBalance.add(remainingBalance.negate());
                }
            }

            var dueDate = DateTimeUtil.addMonthsAndRetrieveNextBusinessDay(disbursementDate, installmentNumber);

            var tax = addTaxIfPresent(taxes, installmentNumber);
            var installmentAmount = pmt.add(tax).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);;

            var installment = InstallmentFactory.withInformation(AmortizationType.PRICE, installmentNumber, InstallmentState.PENDING, dueDate, null, principal, interest, installmentAmount, tax, remainingBalance, taxCompositionIfPresent(taxes, installmentNumber), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

            installment.formatToFinancialTransaction();

            installments.add(installment);
        }

        return installments;
    }
}
