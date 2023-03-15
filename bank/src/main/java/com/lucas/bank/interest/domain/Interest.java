package com.lucas.bank.interest.domain;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lucas.bank.shared.staticInformation.StaticInformation.MONTH_PERIOD;
import static com.lucas.bank.shared.staticInformation.StaticInformation.DAY_TO_YEAR_PERIOD;
import static com.lucas.bank.shared.staticInformation.StaticInformation.YEAR_TO_MONTH_PERIOD;
import static com.lucas.bank.shared.staticInformation.StaticInformation.CALCULATION_PRECISION_SCALE;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Interest {

    private final BigDecimal ratePercentage;
    private final InterestFrequency frequency;

    public static Interest of(BigDecimal rate, InterestFrequency frequency) {
        return new Interest(rate, frequency);
    }

    private static BigDecimal withStaticDecimal(Integer value) {
        var zeros = String.join("", Collections.nCopies(CALCULATION_PRECISION_SCALE, "0"));
        var bigDecimalString = value.toString() + "." + zeros;
        return new BigDecimal(bigDecimalString);
    }

    public static Interest cloneAndConvert(Interest interest, InterestFrequency target) {
        if (interest.frequency == target) return of(interest.ratePercentage, target);

        if (interest.ratePercentage.compareTo(BigDecimal.ZERO) == 0) return of(interest.ratePercentage, target);

        var mathContext = new MathContext(CALCULATION_PRECISION_SCALE);

        if (interest.frequency == InterestFrequency.DAY && target == InterestFrequency.MONTH) {
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = new BigDecimal(MONTH_PERIOD);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        if (interest.frequency == InterestFrequency.DAY && target == InterestFrequency.YEAR) {
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = new BigDecimal(DAY_TO_YEAR_PERIOD);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        if (interest.frequency == InterestFrequency.MONTH && target == InterestFrequency.DAY) {
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = withStaticDecimal(1).divide(new BigDecimal(MONTH_PERIOD.toString()), RoundingMode.HALF_DOWN);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        if (interest.frequency == InterestFrequency.MONTH && target == InterestFrequency.YEAR) {
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = new BigDecimal(YEAR_TO_MONTH_PERIOD);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        if (interest.frequency == InterestFrequency.YEAR && target == InterestFrequency.DAY) {
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = withStaticDecimal(1).divide(new BigDecimal(DAY_TO_YEAR_PERIOD.toString()), RoundingMode.HALF_DOWN);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        if (interest.frequency == InterestFrequency.YEAR && target == InterestFrequency.MONTH) {

            // possible fix - move from year to day and then * 30
            //var daily = interest.getRateToCalculate().divide(new BigDecimal(DAY_TO_YEAR_PERIOD), RoundingMode.HALF_DOWN);
            //var month = daily.multiply(new BigDecimal(30));

            //ToDo - This conversion is wrong - review
            var parsedRate = withStaticDecimal(1).add(interest.getRateToCalculate());
            var multiplier = withStaticDecimal(1).divide(new BigDecimal(YEAR_TO_MONTH_PERIOD.toString()), RoundingMode.HALF_DOWN);
            var res = BigDecimalMath.pow(parsedRate, multiplier, mathContext);
            return of(calculateResult(res), target);
        }

        return null;
    }

    private static BigDecimal calculateResult(BigDecimal result) {
        var parsedResult = result.subtract(BigDecimal.ONE);
        return parsedResult.multiply(withStaticDecimal(100)).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getRateToCalculate() {
        return getRatePercentage().divide(withStaticDecimal(100)).setScale(CALCULATION_PRECISION_SCALE, RoundingMode.HALF_DOWN);
    }

    public List<String> details() {
        List<String> details = new ArrayList<>();

        var dailyInterest = Interest.cloneAndConvert(this, InterestFrequency.DAY);
        var monthlyInterest = Interest.cloneAndConvert(this, InterestFrequency.MONTH);
        var yearlyInterest = Interest.cloneAndConvert(this, InterestFrequency.YEAR);

        details.add("Daily interest: " + dailyInterest.getRatePercentage() + " %");
        details.add("Monthly interest: " + monthlyInterest.getRatePercentage() + " %");
        details.add("Yearly interest: " + yearlyInterest.getRatePercentage() + " %");

        return details;
    }
}
