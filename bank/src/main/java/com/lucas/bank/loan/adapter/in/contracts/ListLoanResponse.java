package com.lucas.bank.loan.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.shared.Metadata;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Value
@Builder
public class ListLoanResponse {
    public List<LoanResponse> loans;
    public Metadata metadata;

    public static ListLoanResponse empty(){
        return new ListLoanResponse(null, Metadata.of(0));
    }

}
