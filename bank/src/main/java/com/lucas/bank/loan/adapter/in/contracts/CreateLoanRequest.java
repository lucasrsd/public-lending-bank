package com.lucas.bank.loan.adapter.in.contracts;

import com.lucas.bank.loan.application.port.in.CreateLoanCommand;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
public class CreateLoanRequest {

    @NotNull
    public Long accountId;

    @NotNull
    public String amortizationType;

    @NotNull
    @Digits(integer = 18, fraction = 2)
    @Min(1)
    public BigDecimal amount;

    @NotNull
    @Range(min = 1, max=10000)
    public Integer term;

    @NotNull
    @Digits(integer = 18, fraction = 10)
    public BigDecimal interestRate;

    @NotNull
    public String interestFrequency;

    public String tax;

    public String disbursementDate;

    public CreateLoanCommand mapToCommand() {
        return CreateLoanCommand
                .builder()
                .accountId(getAccountId())
                .amortizationType(getAmortizationType())
                .amount(getAmount())
                .interestRate(getInterestRate())
                .interestFrequency(getInterestFrequency())
                .term(getTerm())
                .tax(getTax())
                .disbursementDate(LocalDate.parse(disbursementDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay())
                .build();
    }
}
