package com.lucas.bank.interest.domain;

import com.lucas.bank.shared.staticInformation.StaticInformation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lucas.bank.shared.staticInformation.StaticInformation.MONTH_PERIOD;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Accrual {
    public BigDecimal dailyAccrual(BigDecimal amount, Interest interest, Long numberOfdays) {
        BigDecimal dailyInterestRate = Interest.cloneAndConvert(interest, InterestFrequency.DAY).getRateToCalculate();
        BigDecimal dailyAccrual = (amount.multiply(dailyInterestRate)).multiply(new BigDecimal(numberOfdays)).setScale(StaticInformation.PRECISION_SCALE, RoundingMode.HALF_DOWN);
        return dailyAccrual;
    }
}