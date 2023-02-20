package com.lucas.bank.installment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Pmt {

    // Price
    public static BigDecimal price(BigDecimal rate, Integer months, BigDecimal presentValue) {
        BigDecimal result = BigDecimal.ZERO;
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            result = new BigDecimal("-1.00").multiply(presentValue).divide(new BigDecimal(months), RoundingMode.HALF_DOWN);
        } else {
            BigDecimal r1 = rate.add(BigDecimal.ONE);
            BigDecimal opt = BigDecimal.ONE;
            result = presentValue.multiply(r1.pow(months)).multiply(rate).divide(opt.multiply(BigDecimal.ONE.subtract(r1.pow(months))), RoundingMode.HALF_DOWN);
        }
        return result;
    }

    // SAC
    public static BigDecimal sac(Integer months, BigDecimal presentValue) {
        return new BigDecimal("1.00").multiply(presentValue).divide(new BigDecimal(months), RoundingMode.HALF_DOWN);
    }
}
