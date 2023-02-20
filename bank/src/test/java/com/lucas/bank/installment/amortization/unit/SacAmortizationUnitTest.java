package com.lucas.bank.installment.amortization.unit;

import com.lucas.bank.installment.domain.Pmt;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class SacAmortizationUnitTest {

    @Test
    void given_valid_parameters_should_calculate_pmt_sac() {

        BigDecimal presentValue = new BigDecimal("20000");
        Integer months = 10;
        var pmt = Pmt.sac(months, presentValue);
        assertThat(pmt, Matchers.comparesEqualTo(new BigDecimal("2000")));
    }
}
