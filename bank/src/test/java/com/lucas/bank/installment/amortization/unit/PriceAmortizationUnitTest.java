package com.lucas.bank.installment.amortization.unit;

import com.lucas.bank.installment.domain.Pmt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lucas.bank.shared.StaticInformation.PRECISION_SCALE;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class PriceAmortizationUnitTest {

    @Test
    void given_valid_parameters_should_calculate_pmt_price() {

        BigDecimal rate = new BigDecimal("0.005"); // 0.5% MONTH
        BigDecimal presentValue = new BigDecimal("10000").negate();
        Integer months = 3;
        var pmt = Pmt.price(rate, months, presentValue).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);;
        assertThat(pmt, Matchers.comparesEqualTo(new BigDecimal("3366.72208356")));
    }

    @Test
    void given_valid_parameters_2_should_calculate_pmt_price() {

        BigDecimal rate = new BigDecimal("0.03"); // 3% MONTH
        BigDecimal presentValue = new BigDecimal("14225").negate();
        Integer months = 18;
        var pmt = Pmt.price(rate, months, presentValue).setScale(PRECISION_SCALE, RoundingMode.HALF_DOWN);;
        assertThat(pmt, Matchers.comparesEqualTo(new BigDecimal("1034.28119912")));
    }

    @Test
    void given_zero_interest_rate_should_calculate_pmt_price_without_interest() {

        BigDecimal rate = new BigDecimal("0");
        BigDecimal presentValue = new BigDecimal("15000").negate();
        Integer months = 3;
        var pmt = Pmt.price(rate, months, presentValue);
        assertThat(pmt, Matchers.comparesEqualTo(new BigDecimal("5000")));
    }
}
