package com.lucas.bank.interest.unit;


import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.interest.domain.InterestFrequency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class InterestUnitTest {
    @Test
    void given_daily_interest_rate_should_convert_to_month_correct_rates() {

        var dailyInterest = interest_daily_frequency();
        var parsed = Interest.cloneAndConvert(dailyInterest, InterestFrequency.MONTH);
        assertThat(parsed.getRatePercentage(),  Matchers.comparesEqualTo(new BigDecimal("1.54910000")));
        assertThat(parsed.getRateToCalculate(),  Matchers.comparesEqualTo(new BigDecimal("0.01549100")));
    }

    @Test
    void given_monthly_interest_rate_should_convert_to_day_correct_rates() {

        var monthlyInterest = interest_monthly_frequency();
        var parsed = Interest.cloneAndConvert(monthlyInterest, InterestFrequency.DAY);
        assertThat(parsed.getRatePercentage(),  Matchers.comparesEqualTo(new BigDecimal("0.17289000")));
        assertThat(parsed.getRateToCalculate(),  Matchers.comparesEqualTo(new BigDecimal("0.0017289000")));
    }

    @Test
    void given_yearly_interest_rate_should_convert_to_day_correct_rates() {

        var monthlyInterest = interest_yearly_frequency();
        var parsed = Interest.cloneAndConvert(monthlyInterest, InterestFrequency.DAY);
        assertThat(parsed.getRatePercentage(),  Matchers.comparesEqualTo(new BigDecimal("0.04067000")));
        assertThat(parsed.getRateToCalculate(),  Matchers.comparesEqualTo(new BigDecimal("0.0004067000")));
    }

    private Interest interest_daily_frequency(){
        return Interest
                .builder()
                .frequency(InterestFrequency.DAY)
                .ratePercentage(new BigDecimal("0.051254"))
                .build();
    }

    private Interest interest_monthly_frequency(){
        return Interest
                .builder()
                .frequency(InterestFrequency.MONTH)
                .ratePercentage(new BigDecimal("5.318974"))
                .build();
    }

    private Interest interest_yearly_frequency(){
        return Interest
                .builder()
                .frequency(InterestFrequency.YEAR)
                .ratePercentage(new BigDecimal("15.998741"))
                .build();
    }
}
