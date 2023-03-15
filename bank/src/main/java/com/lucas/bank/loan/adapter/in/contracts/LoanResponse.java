package com.lucas.bank.loan.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class LoanResponse {
    public Long loanId;
    public Long accountId;
    public String type;
    public BigDecimal amount;
    public String state;
    public Integer term;
    public String interestFrequency;
    public BigDecimal interestRate;
    public LocalDateTime creationDate;
    public LocalDateTime disbursementDate;
    public LocalDateTime lastAccrualDate;
    public BigDecimal accruedInterest;
    public List<String> additionalInformation;
}
