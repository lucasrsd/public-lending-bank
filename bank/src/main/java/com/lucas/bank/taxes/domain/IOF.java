package com.lucas.bank.taxes.domain;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lucas.bank.shared.StaticInformation.PRECISION_SCALE;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class IOF {
    private static final BigDecimal dailyInterestRate = new BigDecimal("0.000082");
    private static final BigDecimal fixedInterestRate = new BigDecimal("0.0038");

    private static final Long MAXIMUM_DAYS = 365L;

    public List<InstallmentTax> calculate(Date disbursementDate, List<InstallmentDetails> installments) {

        Collections.sort(installments, Comparator.comparing(InstallmentDetails::getNumber));

        List<InstallmentTax> installmentTaxes = new ArrayList<>();

        for (InstallmentDetails installment : installments) {
            long diffMilliseconds =  installment.getDueDate().getTime() - disbursementDate.getTime();
            var daysDifference = TimeUnit.DAYS.convert(diffMilliseconds, TimeUnit.MILLISECONDS);

            daysDifference = Math.min(daysDifference, MAXIMUM_DAYS);

            BigDecimal fixedIof = installment.getPrincipalAmount().multiply(fixedInterestRate).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);;
            BigDecimal dailyIof = installment.getPrincipalAmount().multiply(dailyInterestRate).multiply(BigDecimal.valueOf(daysDifference)).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);;

            Map<String, BigDecimal> composition = new HashMap<>();
            composition.put("FIXED_IOF (" + fixedInterestRate + ")", fixedIof);
            composition.put("DAILY_IOF (" + dailyInterestRate + ")", dailyIof);

            var iofTax = InstallmentTax
                    .builder()
                    .number(installment.getNumber())
                    .totalTaxAmount(fixedIof.add(dailyIof))
                    .composition(composition)
                    .build();

            installmentTaxes.add(iofTax);
        }

        return installmentTaxes;
    }
}
