package com.lucas.bank.interest.domain;

import com.lucas.bank.shared.StaticInformation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lucas.bank.shared.StaticInformation.MONTH_PERIOD;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Accrual {
    public BigDecimal dailyAccrual(BigDecimal amount, Interest interest) {
        BigDecimal dailyInterestRate = Interest.cloneAndConvert(interest, InterestFrequency.DAY).getRateToCalculate();
        var period = new BigDecimal(MONTH_PERIOD.toString());
        BigDecimal dailyAccrual = (amount.multiply(dailyInterestRate)).divide(period, RoundingMode.HALF_DOWN).setScale(StaticInformation.PRECISION_SCALE, RoundingMode.HALF_DOWN);
        return dailyAccrual;
    }
}