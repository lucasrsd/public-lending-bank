package com.lucas.bank.tax.domain;

import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static com.lucas.bank.shared.staticInformation.StaticInformation.CALCULATION_PRECISION_SCALE;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class IOF {
    private static final BigDecimal dailyInterestRate = new BigDecimal("0.000082");
    private static final BigDecimal additionalInterestRate = new BigDecimal("0.0038");

    private static final Long MAXIMUM_DAYS = 365L;

    public List<InstallmentTax> calculate(LocalDateTime disbursementDate, List<InstallmentDetails> installments) {

        Collections.sort(installments, Comparator.comparing(InstallmentDetails::getNumber));

        List<InstallmentTax> installmentTaxes = new ArrayList<>();

        for (InstallmentDetails installment : installments) {

            var daysDifference = Math.min(DateTimeUtil.daysDifference(disbursementDate, installment.getDueDate()), MAXIMUM_DAYS);

            BigDecimal additionalIof = installment.getPrincipalAmount().multiply(additionalInterestRate).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);;
            BigDecimal dailyIof = installment.getPrincipalAmount().multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysDifference)).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);;

            additionalIof = additionalIof.setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE);
            dailyIof = dailyIof.setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE);

            Map<String, BigDecimal> composition = new HashMap<>();
            composition.put("ADDITIONAL_IOF", additionalIof);
            composition.put("DAILY_IOF", dailyIof);

            var iofTax = InstallmentTax
                    .builder()
                    .number(installment.getNumber())
                    .totalTaxAmount(additionalIof.add(dailyIof))
                    .composition(composition)
                    .build();

            installmentTaxes.add(iofTax);
        }

        return installmentTaxes;
    }
}
