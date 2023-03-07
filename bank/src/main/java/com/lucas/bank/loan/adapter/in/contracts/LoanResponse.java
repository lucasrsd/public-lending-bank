package com.lucas.bank.loan.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.loan.domain.Loan;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Date;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date disbursementDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date lastAccrualDate;
    public List<String> additionalInformation;
}
